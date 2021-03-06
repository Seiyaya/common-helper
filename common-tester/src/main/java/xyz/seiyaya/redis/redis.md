# Redis设计与实现
## 数据结构与对象
Redis中每个键值对都是通过字符串`key`和对象`value`组成, 它可以是 字符串、 list 、 hash object 、 set object 、 sorted set object这五种对象中的一种  
### SDS
Redis只会使用C字符串作为字面量，其它的字符串统一使用SDS
```
struct sds{
    // 记录buf数组的长度
    int len;
    // 记录buf中未使用的字节数量
    int free;
    char[] buf;
}
```
+ strlen命令获取字符串长度
+ 相对于C语言的字符串，SDS具有的有点
    - 常数复杂度获取字符串长度,内部维护了len属性，获取复杂度为O(1)
    - 杜绝缓冲区溢出
    - 减少修改字符串长度时所需的内存重分配次数
    - 二进制安全
    - 兼容部分C字符串函数

### List

+ Redis中的链表结构应用场景 --> 对应java中的LinkList
    - 列表键
    - 发布与订阅
    - 慢查询
    - 监视器
```
listNode{
    struct listNode *prev;//前驱节点
    struct listNode *next;//后继节点
    void *value;
}

// redis链表的特性: 双向、无环、带表头指针和表尾指针、带链表长度计数器、多态(可以保存多种不同类型的值)
```
### 字典
+ Redis中的字典结构 --> 对应java中的HashMap
    - 哈希键
    - 因为使用的是渐进式的从ht[0]同步到ht[1]，所以这个过程发生的查询会在两个表上都进行查询
![psync](https://www.seiyaya.xyz/images/notes/redis/struct/hashStruct.png "psync") 
```
dictht{
    dictEntry **table;
    unsigned long size;
    unsigned long sizemask;// 用来计算索引值 = size - 1
    unsigned long used;  // hash表已使用的节点数量
}
dictEntry{
    void *key;
    union{
        void *val;
        uint64_tu64;
        int64_ts64;
    }v;
    // 指向下个hash表的节点，形成链表(多个节点hash值相同的时候会形成，也就是解决哈希冲突的方式)
    struct dictEntry *next;
}
dict{
    // 类型特定函数，针对不同类型的键值对为创建多态字典而设置
    // dictType保存了一簇用于操作特定类型键值对的函数，redis为不同类型的字典设置不同类型特定函数
    dictType *type;
    //私有数据，保存的是需要传给特定函数的可选参数
    void *privadata;
    // hash表，有两个hash表的原因是ht[1]会在rehash的时候使用
    dictht ht[2];
    // rehash索引，记录的是rehash的进度，当rehash不再进行时，值为-1
    int reshshidx;
}
dictType{
    // 计算hash值的函数
    unsigned int(*hashFunction)(const void *key);
    //复制键的函数
    void *(*keyDup)(void *privdata, const void *key);
    // 复制值的函数
    void *(*valDup)(void *privdata, const void *obj);
    // 对比键的函数
    int (*keyCompare)(void *privdata, const void *key1, const void *key2);
    // 销毁键的函数
    void (*keyDestructor)(void *privdata, void *key);
    // 销毁值的函数
    void (*valDestructor)(void *privdata, void *obj);
}
```

+ hash算法
```
// 计算hash值
hash = dict -> type -> hashFunction(key);
// 计算存储元素在hash表的位置,这里使用的是&进行取余，所以要求sizemask必须是2的幂次，即 hash % sizemask = hash & (2^n -1)
index = hash & dict -> ht[x].sizemask;
```

+ rehash
当hashHt存储的元素过多的时候会进行rehash操作，避免hash冲突形成链表  
    - 首先为ht[1]哈希表分配空间，hash表的大小取决于是进行扩展操作还是收缩操作
    - 将ht[0]的元素都rehash到ht[1],将ht[1]设置为ht[0]
程序自动开始扩展操作
    - 服务器目前没有执行BGSAVE命令或者BGREWRITEAOF命令，并且hash表的负载因子>=1
    - 正在执行上述两个命令，并且hash表的负载因子>=5
    - hash_factor = ht[0].used / ht[0].size
    - hash_factor < 0.1时会进行收缩操作
+ 渐进式rehash
    - rehash的过程都是分为多步完成的，避免rehash的数据量过大导致服务停止
    - 原理是使用`reshshidx`来进行标记当前的rehash进度
    - 期间对于hash表的操作会变量ht数组操作

### 跳跃表
+ Redis中的跳表
    - 通过每个节点维护多个指向其它节点的指针，从而达到快速访问的目的
    - 跳表平均O(log n)最坏的情况O(n),大多数情况下跳表和平衡树相媲美，且跳跃表比平衡树简单
    - 使用场景: 一个有序集合包含的元素数量比较多，集合中的元素是长度较长的字符串
    - 应用到跳跃表的只有两个地方，一个是`有序键集合`和`集群节点中用作内部数据结构`
```
// 跳跃表节点
zSkipListNode{
    zSkipListLevel{
        zSkipListNode *forward;// 前进指针
        int span;// 跨度
    }
    zSkipListNode * backward;
    double score;
    robj *obj;
}

// 保存跳跃表节点的相关信息
zSkipList
```

### HyperLogLog
+ 提供的命令 `pfadd` 和 `pfcount`

### Redis中的整数集合(int set)
```
sadd numbers 1 3 5 7 9
object encoding numbers 

intset{
    // 编码方式
    uint32_t encoding;
    // 集合包含的元素数量
    uint32_t length;
    // 保存元素的数组
    int8_t contents[];
}
整数集合每个元素都是intset中的contents中的一项
```

### Redis中的压缩列表
是列表键和hash键的底层实现之一，列表键只包含少量的列表项，并且每个列表项要么是小整数值，要么就是比较短的字符串，那么redis就会用压缩列表来实现
```
rpush list 1 3 5 100886 "hello" "world"
object encoding list

redis3.2 之后的版本打印的是quicklist
```
### 对象
通过之前的数据结构结构创建的对象系统，包含字符串、列表、哈希、集合对象和有序集合  
+ 对象的类型和编码
    - 创建一个键值对产生两个对象，键总是一个字符串对象
+ Redis中的对象
```
redisObject{
    // 对象类型：字符串(REDIS_STRING)、列表(REDIS_LIST)、哈希(REDIS_HASH)、集合(REDIS_SET)、有序集合(REDIS_ZSET)
    unsigned type;
    // 对象使用的编码类型
    unsigned encoding;
    void *ptr;// 指向对象底层实现的数据结构,这些数据结构由encoding决定
    unsigned refCount;// 记录引用数
    unsigned lru;// 记录了对象最后一次被访问的时间
}

// 相关命令,前者返回的是值对象的类型，后者是查看值对象的编码
type {key}
object encoding {key}
// 普通的字符串对象  第一个打印的  string  第二个打印的embstr
```

+ 字符串对象
    - 字符串对象可以是int 、 raw 和 embstr , 其中int表示ptr类型是Long类型，raw表示字符串长度大于32，embstr表示字符串长度小于32  
    - embstr对象是只读的，每次对它的操作都将先转换为raw类型，然后再进行相关操作  
    - embstr对象是连续的

+ 列表对象
列表对象可以是`ziplist`或者`linkedlist`
    - ziplist: 每个压缩节点保存一个元素，所有的元素长度都小于64字节，保存的元素数量小于512个，否则采用的是linkedlist编码
    - redis3.2之后使用的都是`quicklist`,这是一个双向链表

+ 哈希对象
哈希对象可以是`ziplist`或者`hashtable`
    - 添加的元素超过512个的时候会转换为hashtable,且每个元素的字节数小于64字节

+ 集合对象
集合对象可以是`intset`或者`hashtable`
    - 集合对象类型是`intset`的条件必须所有元素都是整数，且元素数量<=512(这个值可以被修改)

+ 有序集合对象
有序集合对象可以是`ziplist`或者`skiplist`
    - ziplist的使用条件，元素数量小于128个，且每个元素的长度小于64字节

+ 内存回收
内部使用引用计数法进行内存的回收

+ 对象共享
对象引用计数属性还具备对象共享的作用，也就是对于keyA和keyB会共用相同的value "100"  
对象共享的操作，将ptr指针指向已经存在的对象，对应的引用计数+1，默认初始化0~10000，这里和Java的Integer有点类似  
使用命令`object refcount`指令查看键A的值对象的引用计数，集合的列表对象都可以进行共享对象  

+ 对象的空转时长
    - `object idletime {key}`可以打印出给定键的空转时间，当前时间-对应的`lru`时间计算  
    - 如果打开了回收内存的算法`volatile-lru`或者`allkeys-lru`,那么服务器占用的内存超过`maxmemory`选项所设置的值时，空转时长较高的那一部分会优先被淘汰


## 单机数据库的实现
### other
+ 使用keys *如果数据量大会导致当前线程不能处理其他问题，其他命令会阻塞
    - scan命令通过游标实现，不会阻塞线程，复杂度也是o(n)
    - scan提供limit参数，可以控制返回结果集，虽然只是一个参考数量
    - scan返回的结果集可能有重复，需要客户端去重
+ scan {cursorIndex} match {matchValue} count {countValue}
    - countValue指的是遍历的字典槽数量
+ 大key扫描
    - redis-cli -p 6388 --bigkeys -i 0.1
+ 单线程为什么快?
    - 所有的数据都是在内存中操作，所有运算也是内存级别的运算
+ 单线程如何处理并发客户端连接
    - 多路复用
+ 事件轮询
    - 非阻塞IO读了一部分数据就返回，线程如何知道何时才继续读，也就是后面的数据到来时，线程怎们才能知道 --> 事件轮询
+ 指令队列
    - 为每个客户端套接字关联一个指令队列，客户端的指令通过队列来排队进行顺序处理
+ 响应队列
    - 为每个客户端关联一个响应队列，redis通过响应队列来将指令的返回结果给客户端。如果队列为空表示连接暂时处于空闲状态，不需要获取写事件
    - 也就是可以将客户端描述符从 write_fds 里面移出来，等到队列有数据再将描述符放进去，避免select系统调用立即返回写事件，但是没有数据可写
+ 定时任务
    - redis的定时任务会记录在一个 最小堆 的数据结构，最快要执行的任务在堆顶。每个周期都会将时间达到的任务执行，得到下一个最快需要执行的任务，这个时间也就是select调用的timeout
+ 通信协议(RESP redis serialization protocol)
    - 是一个文本传输协议，实现、解析简单
### 数据库
+ redis服务器将所有的数据都保存在一个数组当中，db数组的每一项都表示一个数据库
```
redisServer{
    redisDb *db;//一个数组，用来保存服务器中的所有数据库
    int dbnum;// 决定创建多少个数据库，由服务器配置的database决定，默认为16
    saveparams *savaparams; // 保存RDB文件的配置，是一个数组
    long dirty;// 距离上次执行SAVE或者BGSAVE命令，服务器对数据库进行了多少次修改
    time lastsave;// 上一次成功执行SAVE命令或者BGSAVE的时间
    list *clients;// 保存客户端的状态
    redisClient *lua_client;// 执行lua命令的客户端
    int cronloops;// serverCron方法的执行次数

    char *masterhost;// 主服务器的地址
    int masterport;//主服务器的端口

    pid_t rdb_child_pid; // 执行BGSAVE命令的子进程id
    pid_t aof_child_pid; // 执行BGREWRITEAOF的子进程id

    dict *pubsub_channels;// 保存频道的订阅关系
    list *pubsub_patterns;// 保存所有模式订阅信息
}
```

+ 切换数据库: 数据库之间有隔离，默认为0号数据库，可以通过`select`命令来切换到目标数据库
```
redisClient{
    redisDb *db; // 记录客户端正在使用的db,指向redisServer中数组中的一个元素
    
    // 从服务器监听端口号
    int slave_listening_port;
}
```

+ 数据库键空间
读写键时都是先操作dict数组，再根据需求操作对应得value  
这其中也有部分的额外工作，比如维护键空间的命中，hit与miss,通过`info stats`命令查看命中和未命中的次数`keyspace_hits`和`keyspace_misses`  
在读一个键时，服务器会更新键对象的`lru`属性，这个用来计算键的闲置时间  
如果发现键已经过期，那么服务器会删掉这个键然后执行其他操作  
客户端使用`watch`命令监视某个键，那么服务器再对被监视对象进行修改后，会将其标记为dirty，从而让事务程序注意到这个键的信息已经被修改  
服务器每修改一个键之后，dirty计数器都会+1,这个计数器会触发服务器的复制和持久化操作  
如果服务器开启了数据库通知功能，那服务器修改了相关键之后会发送通知给数据库
```
redisDb{
    dict *dict;// 数据库空间，保存所有的键和值
    dict *expires;// 保存键的过期时间

    dict *watched_keys;// 正在被watch监视的键
}

object idletime {key} 查看key的空闲时间
```

+ 设置键的生存时间或过期时间
通过`expire`命令或者`pexpire`命令，`setex`命令主要是设置字符串键的时候可以顺便设置对应的值，原理和前面两个命令一样  
`ttl`命令和`pttl`会返回这个键被自动删除的剩余时间  
```
setex 可以设置一个字符串键的时候同时设置过期时间
expire {key} {ttl}
pexpire {key} {ttl}
expireat {key} {timestamp}
//上述命令都是通过下面这个命令实现的
pexpireat {key} {timestamp}
```


+ 过期键的删除策略
    - 定时删除: 设置一个定时器，在键要过期的时候执行删除
    - 惰性删除: 每次获取键的时候判断是否过期
    - 定期删除: 每隔一段时间进行一次全量的删除过期key
    - redis的过期删除策略: 惰性删除和定期删除

+ Redis的持久化: AOF和RDB  复制
    - 生成RDB文件，在执行`SAVE`或者`BGSAVE`命令创建RDB文件的时候扫描数据库中没有过期的键保存到文件中
    - 载入RDB文件，在启动redis服务器的时候，如果开启了RDB功能则对RDB文件进行载入
        - 服务器以主服务器运行的模式，那么在载入RDB文件的时候只会载入未过期的键
        - 服务器以从服务器运行的模式，文件中的不论是否过期都将被载入到服务器，但是因为主从同步的关系，后面过期键还是会被删除
    - AOF文件写入: 数据库的某个键已经过期不会对AOF产生影响，在被删除的时候会向AOF文件追加一条DEL命令
    - AOF重写: 过期的键不会被写入到AOF文件
    - 复制: 从服务器只有接收到来自主的DEL命令才会删除键，直接查询从不会因为已经过期而查不到
    - 数据库通知: redis2.8之后的功能，可以让客户端通过订阅给定的频道或者模式，来获知数据库中键的变化以及命令执行情况
```
// 数据库通知需要开启notify-keyspace-events属性，默认为空字符串
SUBSCRIBE __keyspace@0__:msg   // 监控键msg的变化
notify-keyspace-events AKE 服务器发送所有的键空间和键事件
notify-keyspace-events AK 服务器发送所有的键空间
notify-keyspace-events AE 服务器发送所有的键事件
notify-keyspace-events K$ 服务器只发送所有的有关字符串键有关的键空间通知
notify-keyspace-events Elg 服务器只发送所有的有关列表键有关的基础命令通知
```
### RDB持久化
+ 命令`SAVE`和`BGSAVE`，一个是同步保存，另外一个是新开一个子进程进行保存 ，载入命令是redis服务启动的时候自动载入的，没有命令支持
    - AOF更新频率通常比RDB文件的更新频率高，所以开启了AOF功能，优先使用AOF来还原数据
    - 只有的AOF功能关闭的时候才使用RDB进行载入
    - SAVE命令的执行是同步的，期间redis服务不会接受其他的请求
+ BGSAVE命令执行的时的服务器状态
    - BGSAVE命令的执行是异步的，但是不能在这个过程中执行SAVE命令，防止竞争条件，BGREWRITEAOF命令正在执行，那么客户端发送的BGSAVE命令会被拒绝
+ RDB文件载入的时候服务器状态
    - 处于阻塞状态，直到载入完成
+ 自动间隔保存RDB文件
```
//BGSAVE执行的条件  900秒内进行了至少1次修改
save 900 1
save 300 10
save 60 10000
saveparam{
    time_t seconds;// 秒数
    int changes; // 修改数
}
```

+ dirty计数器和`lastsave`属性
    - dirty计数器: 记录距离上一次执行SAVE命令之后服务器对数据库进行了多少次修改
    - `lastsave`: 记录上一次执行SAVE命令的间隔时间
+ 检查保存的条件是否满足
    - 检查`saveparams`数组中所有保存的条件，只要有一个满足就会执行SAVE命令,默认为100ms一次
+ RDB文件的结构
    - redis|db_version|databases|EOF|check_sum
    - redis校验头，用来校验载入的文件是否是RDB文件，占用5字节，保存`REDIS`五个字符
    - db_version,记录RDB文件的版本号，4个字节
    - EOF，1个字节标志正文结束
    - databases： 保存有数据的数据库的键值情况
        - SELECTDB: 标志未，表示接下来要读的是数据库的编号
        - db_number: 数据库编号，redisDb的索引号
        - key_value_pairs: 保存键值，包含过期数组expires 格式([EXPIRETIME_MS|ms]type | key | value)
    - check_sum 是对前面几个参数的校验
 
+ RDB中value的存储结构 即databases->key_value_pairs->value
    - 字符串对象 type = REDIS_RDB_TYPE_STRING   type value = REDIS_ENCODING_INT 或者 REDIS_ENCODING_RAW  保存的字符串如果超过20个字节就会被压缩，具体看配置`rdbcompression`
    - 列表对象   type = REDIS_RDB_TYPE_LIST  type value = REDIS_ENCODING_LINKEDLIST
    - 集合对象   type = REDIS_RDB_TYPE_SET  type value = REDIS_ENCODING_HT
    - 哈希表对象 type = REDIS_RDB_TYPE_HASH  type value = REDIS_ENCODING_HT
    - 有序集合对象 type = REDIS_RDB_TYPE_ZSET  type value = REDIS_ENCODING_SKIPLIST
    - INTSET集合 type = REDIS_RDB_TYPE_SET_INSET  将整数集合转为字符串集合然后保存到RDB中
+ 分析RDB文件，可以使用`od`命令  od -c {fileName}

### AOF持久化
通过保存执行的命令来实现持久化，具体的实现功能体现到命令上有: 追加、文件写入、文件同步三个步骤
+ 命令追加: 执行的命令会被添加到`aof_buf`缓冲区的末尾
+ AOF文件的写入和同步
    - redis进程本身是一个事件循环，循环中的文件事件负责接收客户端的文件请求，以及向客户端发送命令回复，执行一次事件循环后即可判断是否把缓冲区中的内容写入到对应的文件中
+ appendfsync属性配置
    - always: 将aof缓冲区中的所有内容写入并同步到AOF文件
    - everysec: 将aof_buf缓冲区的内容写入到AOF文件，如果上次同步的时间距离本次超过一秒钟，那么再次针对AOF同步，这个操作由一个专门的线程执行
    - no: 将aof_buf所有的缓冲区内容写入到AOF文件，但并不对AOF文件进行同步，同步时机由操作系统决定
+ AOF的载入与还原
    - 1. 创建一个不带网络环境的伪客户端(redis的命令只能在客户端的上下文执行)，而AOF载入的源来自于AOF文件不是网络
    - 2. 从AOF文件中分析并读取出一条命令
    - 3. 伪客户端执行读出来的命令
    - 4. 一直重复2-3步骤，直到AOF文件被读完
+ AOF重写: 创建一个新的aof文件，是原来已有aof的精简版，相关命令`BGREWEITEAOF`
    - 本质上是根据服务器的状态来进行修改生成新的aof文件
    - 因为redis是单线程，所以为了同时进行重写AOF以及接受命令请求，采用子进程的方式进行AOF重写，不实用子线程可以避免大量锁的问题
    - AOF重写过程中添加的键值可能保存不到，新增一个AOF重写缓冲区(此时被执行的命令会被添加到重写缓冲区以及aof_buf)
    - 重写完成后，子进程通知父进程，父进程将AOF重写缓冲区的内容写入到AOF文件，对新的AOF文件进行改名处理，完成新旧交替，父进程只有在接收到通知后会阻塞一次
    
### 事件
文件事件: redis服务器通过Socket与客户端进行连接，文件事件是对Socket操作的抽象   
时间事件: redis中的一些操作需要指定时间点执行(serverCron函数)  

#### 文件事件:
+ redis基于reactor模型开发的网络事件处理器，这个事件呗称为文件事件处理器
    - 使用IO多路复用模型监听多个套接字，并根据套接字目前执行的任务为套接字关联不同的处理器
    - 当监听的套接字准备好执连接应答accept、read、write、close等操作时，这时文件处理器就会套接字之前关联好的事件处理器处理事件
    - 多个文件事件并发出现IO多路复用模型会将产生事件的`套接字`放到一个队列，有序、同步每次一个套接字的方式向文件事件派发器产送套接字

+ IO多路复用模型的实现
    - IO多路复用模型主要是包装select、epoll、evport和kqueue这些IO多路复用函数库实现，可以根据不同常见选择不同的IO模型

#### 时间事件  
+ 定时事件: 让程序在一段指定时间之后执行
+ 周期性事件: 循环某个点执行 , redis目前使用的时间事件类型
+ 周期性事件的实现: 将所有的时间事件放到一个无序列表中，每当时间事件执行器运行时，它就遍历整个链表，查找所有已到达的时间事件，并调用相应的事件处理器


### 客户端
#### 客户端属性
+ redis客户端结构(redisClient)
    - 客户端的套接字描述符
    - 客户端的名字
    - 客户端的标志值
    - 指向客户端正在使用的数据库指针，以及该数据库的号码
    - 客户端当前要执行的命令、命令的参数、命令参数的个数，以及指向命令实现函数的指针
    - 客户端的输入缓冲区和输出缓冲区
    - 客户端的复制状态、以及进行复制所需要的数据结构
    - 客户端执行brpop、blpop等列表阻塞命令时使用的数据结构
    - 客户端执行发布与订阅功能时用到的数据结构
    - 客户端的身份验证标志
    - 客户端的创建时间，客户端与服务器端最后一次通信时间，以及客户端的输出缓冲区大小超出软性限制的时间
+ 客户端的属性
    - 通用属性: 无论执行的是什么工作，它们都要用到这些属性
    - 特殊属性: 比如操作数据库时所需要用到的db属性和dictId属性，执行事务时需要的`mstate`属性，以及执行watch命令需要用到的`watched_keys`属性
+ 套接字描述符号
    - 客户端状态的fd属性记录了客户端正在使用的套接字描述，伪客户端是-1，正常为大于-1
        - 伪客户端处理的命令请求主要来源于AOF文件或者Lua脚本而不是网络，不需要建立套接字连接
        - `client list`查看连接的信息
+ 客户端的名字: 默认情况下连接到服务器的客户端是没有名字的，使用`client setname {name}`为客户端设置名称
+ 客户端的标志值: 记录了客户端的角色，以及客户端目前的所处状态
    - 主从复制的时候，主从对对方而言都是属于客户单，`REDIIS_MASTER`标志表示客户端代表的是一个主，`REDIS_SLAVE`标志表示客户端代表的一个从服务器
+ 输入缓冲区: 客户端状态的输入缓冲区用于保存客户端发送的命令请求
+ 命令与命令参数
    - argv   set key value 其中argv[0]=set  |  argv[1] = key  |  argv[2] = value
    - argc = 3
    - 命令的函数是实现: 对应客户端的redisCommand属性
+ 输出缓冲区: 一个可变缓冲区，一个固定缓冲区
    - 固定缓冲区: 保存固定长度且长度小的回复，如ok、数值回复等，由`buf`和`bufpos`组成
    - 可变缓冲区: 保存长度较大的回复
+ 身份认证
+ 时间
```
redisClient{
    int fd;// 客户端状态
    int flags;// 客户端所处的状态
    sds querybuf;// 输入缓冲区，最大不能超过1GB

    robj **argv;// 命令参数
    int argc;// 命令参数的个数
    struct redisCommand *cmd;

    char buf[REDIS_REPLY_CHUNK_BYTES];// 用来存储回复，默认大小为16*1024，即16KB
    int bufpos;// 记录已经使用的大小
    list *reply;// 可变缓冲区，通过链表链接多个字符串对象

    int authenticated;// 0表示未认证1已通过认证，为0时除了auth命令都会被拒绝

    time_t ctime;// 客户端创建时间
    time_t lastinteraction;// 客户端与服务端最后一次交互时间，包含请求与响应
    time_t obuf_soft_limit_reached_time;// 输出缓冲区第一次达到软限制的时间
}
```

#### 客户端的创建与关闭
客户端的创建与关闭分为普通和伪，伪主要是作为lua脚本和aof载入的时候使用
+ 普通客户端的创建
    - 客户端调用connect方法建立网络连接，服务器调用相关事件处理器，为客户端创建相应的客户端状态，并将这个状态添加到clients末尾
+ 普通客户端的关闭: 客户端的关闭有多种原因
    - 客户端进程退出或者被kill,客户端和服务端之间的网络连接将会关闭，从而造成客户端被关闭
    - 如果客户端向服务端发送了不符合格式的请求，也会关闭客户端
    - 如果客户端设置为了client kill命令的目标，也会被关闭
    - 如果用户为服务端设置了timeout选项，客户端的空转时间超过值也会关闭，主从和发布订阅的情况除外
    - 如果客户端发送请求大小超过了输入缓冲区大小(1GB)
    - 如果客户端发送请求大小超过了输出缓冲区大小

+ lua脚本的伪客户端
    - 服务端会在初始化时创建负责执行lua脚本的伪客户端，并将这个伪客户端关联在服务器结构的`lua_client`属性上
    - lua客户端的声明周期和redisServer一样
 
+ AOF文件的客户端
    - 创建实际为载入AOF文件的时候，关闭的实际在载入AOF文件完毕

#### 服务器
+ 命令请求的执行过程
    - 1. 客户端发送`set key value`命令到服务器端,主要是将命令转换为对应的协议格式发送
    - 2. 服务端接受并处理客户端发来的命令请求`set key value`，在数据库中进行设置操作，并产生命令回复ok
        - 客户端写入将服务的状态变为可读时，服务器将调用命令请求处理器来执行一下操作
            - 1). 将请求转换为客户端状态的输入缓冲区
            - 2). 对输入缓冲区中的命令请求进行解析，提取出命令请求中包含的命令参数。以及命令的参数个数(对应argv和argc)
            - 3). 调用命令执行器，执行客户端指定的命令
        - 服务器将通过调用命令执行器完成执行命令所需的余下步骤
        - 查找命令: 查找argv[0]在命令表中的对象实现
        - 执行预准备操作: 预准备操作，保证稍后执行的前置条件正常。这些操作包括，检查cmd指针、检查是否通过了验证、开启了maxmemory检查是否达到了最大内存等等一系列校验
        - 调用命令的实现函数
        - 执行后序工作:开启了慢查询日志决定是否添加校验，AOF持久化、更新redisCommand的milliseconds属性、其它服务器正在复制当前服务器该命令也发送过去
    - 3. 服务器将命令回复ok发送给客户端
        - 将命令的回复保存到客户端的输出缓冲区里面，并为客户端的套接字关联命令回复处理器，将客户端套接字变为可写的状态
    - 4. 客户端接受服务器返回的命令回复ok,并将这个回复打印给用户
```
redisCommand{
    char *name;// 命令的名称，比如set
    redisCommandProc *proc;// 指向命令的实现函数
    arity int;// 命令参数的个数，用于检查命令格式是否正确
    sflags char*;// 字符串标识形式,记录这个命令的属性
    flags int;// 对sflag二进制的标识
    calls long long;// 服务器总共执行了多少次这个命令
    milliseconds long long;// 服务器执行这个命令所耗费的总时长
}
```

+ serverCron函数: redis服务器中的serverCron函数默认每隔100毫秒执行一次，这个函数负责管理服务器的资源，并保持服务器自身的良好运转  
    - 更新服务器时间的缓存
    - 更新LRU时钟
    - 更新服务器每秒执行命令次数
    - 更新服务器内存峰值的记录: stat_peak_memory记录着已使用内存的峰值
    - 处理sigterm信号
    - 管理客户端资源: 都会调用clientsCron函数，检查连接是否超时，优化输入缓冲区
    - 管理数据库资源: 都会调用databaseCron，删除过期键
    - 执行被延迟的`BGREWRITEAOF`
    - 检查持久化操作的运行状态: 服务器状态使用rdb_child_pid属性和aof_child_pid属性记录执行BGSAVE命令和BGREWRITEAOF命令是否正在执行
    - 将AOF缓冲区中的内容写入AOF文件
    - 关闭异步客户端: 输出缓冲区超过限制的客户端
    - 增加`cronloops`计数器的值: serverCron函数的执行次数，主要是用来每执行{x}次就调用特定方法

+ 服务器初始化
    - 初始化服务器状态结构: 初始化redisServer结构体,主要包含设置运行id、默认运行频率、默认配置文件路径、运行架构、端口号、RDB和AOF持久化条件、LRU时钟、命令表
    - 载入配置选项: 载入redis.conf配置文件
    - 初始化服务器数据结构: 包含server.clients链表、server.db数组、server.pubsub_channels、server.pubsub_patterns链表、server.lua、server.slowlog
    - 还原数据库的状态: 载入RDB文件或者AOF文件
    - 执行事件循环

## 多机数据库的实现

### 复制
redis中可以通过`SLAVEOF`命令或者设置slaveof选项，让一个服务器复制领一个服务器，被复制的为主，复制的为从
+ redis V2.8之前的复制实现
    - 主要功能有`同步`和`命令传播`两个操作
    - 同步: 客户端想服务端发送`SLAVEOF`命令，要求从服务器复制主服务器，从服务器首先需要执行同步操作
        - 1). 从服务器向主服务器发送SYNC命令
        - 2). 收到SYNC命令的住服务器执行BGSAVE命令，并记录之后执行的命令到缓冲区,将生成的RDB文件发给从服务器，从而达到将主服务器的状态同步到从服务器
        - 3). 再同步缓冲区中的命令
    - 命令传播: 客户端发给主的命令也需要发给从来执行
    - 旧版本复制的缺陷: 复制分为初次复制和断线后重复制(主要的问题就是断线后重复制)

+ 新版复制功能的实现(`PSYNC`)
    - redis2.8之后使用PSYNC命令代替SYNC命令执行同步操作，分为完整重同步和部分重同步，和前面的区别主要是部分重同步
    - 部分重同步: 主服务器的复制偏移量和从服务器的复制偏移量、主服务器的复制积压缓冲区、服务器运行的id三个部分组成
    - 复制偏移量: 主从都会维护一个复制偏移量，标记复制的地方    
    - 复制积压缓冲区: 主维护的一个固定长度先进先出队列，默认为1M，保存的是复制过程中新增的命令，主要是用来断线重连后保持数据的完整性
    - 服务器运行ID: 首次复制的时候从服务器会保存主服务器的id信息，用来决定格式完整的复制还是部分的复制

+ PSYNC命令的实现
![psync](https://www.seiyaya.xyz/images/notes/redis/multi-database/PSYNC.png "psync") 
    - 主服务器A和从服务器B，B第一次复制A，B向A发送`PSYNC ? -1`命令,请求A进行完整重同步操作
    - A接收到完整同步的请求后，将在后台执行`BGSAVE`命令，并向B返回`+ FULLRESYNC {runid} {offset}`回复
    - 在同步过程中网络断开，B再连上的时候发送命令`PSYNC {runid} {newOffset}`请求进行部分重同步
    - A收到B发送的runid后和自身的runid进行比较来决定是全同步还是部分同步
    
+ 复制的实现
    - 1. 设置主服务器的地址和端口: redisServer中masterHost和masterPort属性
    - 2. 建立套接字连接: 从服务器将为建立好的套接字专门用一个处理复制工作的文件事件处理器，这个处理器负责后序的复制工作(接受RDB文件以及后序的命令传播)
    - 3. 发送PING命令: 从向主发送PING命令，主要是用来检测套接字是否能正常使用、检查主是否能正常处理命令请求，PING命令请求超时或者返回的不是PONG，则从进行断开重连
    - 4. 身份验证: `masterauth`开启才进行验证
    - 5. 发送端口信息: 发送从监听的端口`REPLCONF lisening-port {port-number}`,主接收到会记录到redisClient的 slave_listening_port 属性中
    - 6. 同步
    - 7. 命令传播
+ 心跳检测: 在命令传播阶段，从默认以间隔一秒的向主发送`REPLCONF ACK {replication_offset}`
    - 检测主从服务器之间的网络连接状态
    - 辅助实现min-slaves配置选项
    - 检测命令丢失
    
### sentinel(哨兵)
是redis高可用解决方案，由一个或多个Sentinel实例组成的Sentinel系统可以监视任意多个主服务器，主服务器下线后，从服务器会取代称为新的主服务器
```
// 启动一个Sentinel可以使用命令
redis-sentincel sentinel.conf
redis-server sentinel.conf --sentinel
```
#### 启动sentinel需要执行的步骤
+ 1. 初始化服务器： sentinel本质上只是一个运行在特殊模式下的redis服务器，初始化的虽然是redis服务器，但是初始化步骤不一样，比如不会载入RDB和AOF文件
    - 1). 使用sentinel专用代码: 启动的时候是单独的端口号，也没有载入命令表等数据
    - 2). 初始化sentinel状态: 
    - 3). 初始化sentinel状态的master属性: 
    - 4). 创建连向主服务器的网络连接: Sentinel向主从服务器发送命令，对于主会创建两个连接(一个是命令连接，一个是订阅连接)
+ 2. 获取主服务器信息: 默认以10/s一次的频率通过命令连接向被监视的主服务器发送INFO命令(同时也可以得到主下面的从服务器)
+ 3. 获取从服务器信息: 为从服务器也创建订阅连接和命令连接
+ 4. 向主服务器和从服务器发送信息: 默认情况下是2/s每次的频率，通过命令连接向所有被监视的主从服务器发送
+ 5. 接收来自主服务器和从服务器的频道信息
    - 更新sentinels字典: 多个Sentinels会进行广播，如果是自己发送的丢弃接收到的消息，非己的则更新主服务器的实例结构
    - 创建连向其他Sentinel的命令连接: 多个Sentinel会建立命令连接到其他的Sentinel，但是不会创建订阅连接
+ 6. 检查主观下线状态: 每秒ping命令连接，判断对应的实例是否在线(实例包含主从、sentinel)
+ 7. 检查客观下线状态: 修改为主观下线后判断是否真的下线，向监视器询问，他们的判定结果，然后指向故障转移的操作
    - 发送sentinel is-master-down-by-addr命令，询问其他sentinel是否同意主服务器下线
    - `sentinel is-master-down-by-addr {ip} {port} {current_epoch} {runid}`
    - 接受`sentinel is-master-down-by-addr`命令，根据请求的内容判断对应主服务器是否下线，回复命令`sentinel is-master-down-by-addr`
+ 8. 选举领头Sentinel
+ 9. 故障转移
    - 在已下线的从服务器选出一个主服务器
    - 原有的从服务器复制这个新的主服务器
    - 将原有的从服务器设置为这个新选出的主服务器的从服务器，原有的主服务器上线后还原以上操作
```
sentinelState{
    uint64_t current_epoch;//当前纪元，用于实现故障转移
    dict *masters;//保存了所有被这个sentinel监视的主服务器，key-->主服务器的名字，value-->指向sentinelRedisInstance结构的指针
    
    int tilt;// 是否进入了TILT模式
    int running_scripts;// 正在执行脚本的数量
    mstime_t title_start_time;// 进入TILT的时间
    mstime_t previous_time;// 最后一次执行时间处理器的时间
    list *scripts_queue;// 一个FIFO队列，包含了所有需要执行的用户脚本
}
sentinelRedisInstance{
    int *flags;// 标识值，记录了实例的类型，以及该实例的当前状态
    char *name;// 实例的名字，主服务器的名字由用户在配置文件中配置，从服务器由Sentinel配置
    char *runid;// 实例运行的id
    uint64_t config_epoch;// 配置纪元，用于实现故障转移
    sentinelAddr *addr;// 实例的地址，存放的是ip和端****口信息

    mstime_t down_after_period;// 实例无响应多少毫秒判定为主观下线
    int quorum;// 判断当前实例客观下线所支持的票数
    int parallel_syncs;// 在进行故障转移的时候，可以同时对新的主服务器进行同步的从服务器数量
    mstime_t failover_timeout;// 刷新故障迁移状态的最大时限
}
```
### 集群
集群通过分片的方式来进行数据共享，并提供复制和故障转移的功能  

#### 节点
一个redis集群通常由多个节点组成，刚开始都是相互独立的，需要手动建立起连接   
+ `CLUSTER MEET {ip} {port}`    可以让node节点与ip和port所指定的节点握手，通过握手使得所有的应用加入到一个集群中
+ 启动节点: 通过配置属性`cluster-enabled`配置选项是否为yes来决定是否开启服务器集群模式
    - 启动后的redis会继续使用单机模式下的组件，比如文件事件处理器处理命令请求和返回命令回复，继续执行serverCron函数(会调用集群特有的clusterCron进行心跳检测以及故障转移等)

+ 集群的数据结构: clusterNode保存了当前节点状态。比如节点的创建时间、节点的名字、节点的当前配置纪元、节点的ip地址等
+ cluster meet命令的实现
    - 节点A会为节点B创建一个clusterNode结构，并将该结构添加到自己的clusterState.nodes字典里面
    - 节点A根据CLUSTER MEET命令给定的IP地址和端口号，向节点B发送一条MEET消息
    - 节点B收到节点A发送的MEET消息，节点B会为节点A创建一个clusterNode结构，并将结构添加到clusterState.nodes字典里面
    - 节点B会响应pong消息
    - 节点A将接收到节点B返回的PONG消息，通过这条PONG消息节点A知道节点B成功收到MEET消息
    - 节点A想节点B返回一条PING消息
    - 节点B接收来自A返回的PING消息，通过这条PING消息节点B可以知道A已经成功接收到了自己返回的PONG消息，握手完成
    - 最后节点A会将节点B的信息通过Gossip协议传播给集群中的其他节点，让其他节点也与B握手
```
clusterNode{
    mstime_t ctime;// 创建节点时间
    char name[REDIS_CLUSTER_NAMELEN];
    int flags;// 记录节点的角色
    uint64_t configePoch;// 配置当前的配置纪元，用于实现故障转移
    char ip[REDIS_IP_STR_LEN]; // ip地址
    int port;
    clusterLink *link;// 保存连接节点所需的有关属性

    // 槽位相关
    unsigned char slots[16384/8];
    int numslots;
}
clusterLink{
    mstime_t ctime;// 连接的创建时间
    int fd;// TCP套接字描述符
    sds sndbuf;// 输出缓冲区
    sds rcvbuf;// 输入换从去，保存其他节点接收到的消息
    struct clusterNode *node;
}
clusterState{
    clusterNode *myself;
    uint64_t currentEpoch;
    int state; // 集群的当前状态，在线或者下线
    int size;// 集群中至少处理着一个槽的节点数量
}
```

#### 槽指派
redis集群通过分片的方式保存数据库中的键值对，集群被划分为16384个槽(solt),集群中的每个节点都可以处理[0,16384]槽  
如果每个槽都是正常工作，则说明集群处于正常，主要有一个不正常，那集群表现为下线状态，`CLUSTER INFO`查看集群的状态，前提是开启了集群   
+ 开启集群需要注意的点
    - 开启集群的应用需要关闭`SLAVEOF`属性，slaveof directive not allowed in cluster mode  
    - 开启集群的应用需要设置`cluster-config-file`属性,否则启动不了
+ 给指定节点分配槽位 `CLUSTER ADDSLOTS 0 1 2 3 4 ... 5000`
+ 记录节点的槽指派信息
    - clusterNode的slots属性

#### 命令执行
+ 计算键属于哪个槽,使用命令`cluster keyslot {key}`获取key是在哪一个槽中
+ 判断槽是否由当前节点负责
    - clusterState.slots[i] = clusterState.myself,节点直接执行客户端发送的命令
    - 否则根据clusterState.slots[i]指向的clusterNode结构所记录的IP和端口，向客户端发送MOVED错误
    - MOVED错误 `MOVED {slot} {ip}:{port}`,集群下使用单机模式，接收到MOVED会直接打印错误
#### 重新分片
redis集群的重新分片操作可以将任意数量已经指派给某个节点(源节点)的槽改为指派到另外一个节点(目标节点)，槽的键值也会移到目标节点

#### 转向
#### 故障转移
#### 消息

## 独立功能的实现

### 发布与订阅
通过publish、subscribe、psubscribe命令实现发布订阅功能
+ 频道的订阅与退订
    - 客户端执行`subscribe`命令订阅一个或多个频道的时候，客户端与被订阅频道之间就建立了一种订阅关系，key是订阅的频道，value是链表存储着订阅该频道的客户端
    - 频道的退订: unsubscribe命令的行为与subscribe刚好相反，即删除对应的链表上的节点

+ 模式的订阅和退订(redisServer的属性 pubsub_patterns)
    - 是一个链表结构，每个节点都包含pubsub Pattern结构，这个pattern记录了被订阅的模式，而client属性记录了订阅模式的客户端
    - 模式订阅: 新建一个`pubsubPattern`结构，将pattern设置为被订阅的模式，client的属性设置为订阅模式的客户端
       - 将pubsubPattern添加到pubsub_patterns的队尾
    - 模式退订和频道退订操作差不多

+ 发送消息`publish {channel} {message}`
    - 将message发送给频道的所有订阅者
    - 模式匹配的客户端也会收到消息 
```
pubsubPattern{
    redisClient *client; // 比如 client-9
    robj *pattern;// 比如 news.*
}

pubsub channels // 用来返回当前服务器被订阅的频道
pubsub numsub   // 用来返回服务器当前被订阅频道的订阅者数量
pubsub numpat   // 用来返回服务器当前被订阅模式的数量，这个命令是通过pubsub_partterns链表的长度来实现
```

### 事务
redis通过multi、exec、watch等命令来实现事务，执行该客户端的一系列命令中不会执行其他客户端的命令

+ 通过队列实现的事务
    - 事务开始
    - 命令入队
    - 事务执行
   
+ watch命令: 乐观锁的一种实现
    - 可以监控一个键是否在提交前被修改过，在redisDB上属性watched_keys,key->监视的键，value->监视的客户端

### 二进制位数组
redis提供了setbit 、 getbit 、 bitcount 、 bitop四个命令用于处理二进制位数组  
+ 数据结构: 使用字符串来存储二进制位数组


### 慢查询日志 

### 监视器