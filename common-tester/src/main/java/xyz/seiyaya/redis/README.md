# Redis设计与实现
## 数据结构与对象
Redis中每个键值对都是通过字符串`key`和对象`value`组成, 它可以是 字符串、 list 、 hash object 、 set object 、 sorted set object这五种对象中的一种  
+ Redis使用了结构体来定义字符串(simple dynamic string)
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

+ Redis中的链表结构应用场景 --> 对应java中的LinkList
    - 列表键
    - 发布与订阅
    - 慢查询
    - 监视器
+ Redis中的字典结构 --> 对应java中的HashMap
    - 哈希键

+ Redis中的跳表
    - 通过每个节点维护多个指向其它节点的指针，从而达到快速访问的目的
    - 跳表平均O(log n)最坏的情况O(n),大多数情况下跳表和平衡树相媲美，且跳跃表比平衡树简单
    - 使用场景: 一个有序集合包含的元素数量比较多，集合中的元素是长度较长的字符串
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

+ Redis中的整数集合
+ Redis中的压缩列表
+ Redis中的对象
```
redisObject{
    unsigned type;// 对象类型：字符串、列表、哈希、集合
    unsigned encoding;
    void *ptr;// 指向对象底层实现的数据结构,这些数据结构由encoding决定
    unsigned refCount;// 记录引用数
    unsigned lru;// 记录了对象最后一次被访问的时间
}

// 相关命令  type {key}
object encoding {key}
```

+ 字符串对象
字符串对象可以是int 、 raw 和 embstr , 其中int表示ptr类型是Long类型，raw表示字符串长度大于32，embstr表示字符串长度小于32  
embstr对象是只读的，每次对它的操作都将先转换为raw类型，然后再进行相关操作  

+ 列表对象
列表对象可以是`ziplist`或者`linkedlist`

+ 哈希对象
哈希对象可以是`ziplist`或者`hashtable`

+ 集合对象
集合对象可以是`intset`或者`hashtable`

+ 有序集合对象
有序集合对象可以是`ziplist`或者`skiplist`

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
### 数据库
+ redis服务器将所有的数据都保存在一个数组当中，db数组的每一项都表示一个数据库
```
redisServer{
    redisDb *db;//一个数组，用来保存服务器中的所有数据库
    int dbnum;// 决定创建多少个数据库，由服务器配置的database决定，默认为16
    saveparams *savaparams; // 保存RDB文件的配置，是一个数组
    list *clients;// 保存客户端的状态
    redisClient *lua_client;// 执行lua命令的客户端
    int cronloops;// serverCron方法的执行次数

    pid_t rdb_child_pid; // 执行BGSAVE命令的子进程id
    pid_t aof_child_pid; // 执行BGREWRITEAOF的子进程id
}
```

+ 切换数据库: 数据库之间有隔离，默认为0号数据库，可以通过`select`命令来切换到目标数据库
```
redisClient{
    redisDb *db; // 记录客户端正在使用的db,指向redisServer中数组中的一个元素
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
}
```

+ 设置键的生存时间或过期时间
通过`expire`命令或者`pexpire`命令，`setex`命令主要是设置字符串键的时候可以顺便设置对应的值，原理和前面两个命令一样  
`ttl`命令和`pttl`会返回这个键被自动删除的剩余时间


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
    - 复制: 从服务器只有接受到来自主的DEL命令才会删除键，直接查询从不会因为已经过期而查不到
    - 数据库通知: redis2.8之后的功能，可以让客户端通过订阅给定的频道或者模式，来获知数据库中键的变化
### RDB持久化
+ 命令`SAVE`和`BGSAVE`，以个是同步保存，另外一个是新开一个子进程进行保存 ，载入命令是redis服务启动的时候自动载入的，没有命令支持
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
### sentinel
### 集群

## 独立功能的实现

### 发布与订阅
### 事务
### Lua脚本
### 排序
### 二进制位数组