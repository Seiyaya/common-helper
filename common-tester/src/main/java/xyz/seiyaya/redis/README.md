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