# 第一章 分布式架构
## 集中式到分布式
+ 分布式: 一个硬件或者软件分布在不同的网络计算机上，彼此间仅仅通过消息进行通信和协调的系统
+ CAP定律: 分布式系统不可能全部满足，最多只能满足其中的两点
    - 一致性(Consistency): 多个系统之间的数据保持一致
    - 可用性(availability): 每个操作请求都能在有限时间内返回结果
    - 分区容错性(partition tolerance): 一部分机器故障，但是分布式系统对外仍为正常
+ BASE理论:
    - 基本可用(basically available): 系统出现故障时允许损失部分的可用性(响应时间损失、功能损失)
    - 软状态(soft state): 允许系统中的数据存在中间状态，也就是数据在不同机器上可以存在一定的延时
    - 最终一致性(eventually consistent):所有的数据经过一段时间的同步，最终数据会保持一致
        - 因果一致性
        - 读已之一致性
        - 会话一致性
        - 单调读一致性
        - 单调写一致性
# 第二章 一致性协议
分布式系统中一个机器无法知道其他机器的事务提交情况，需要引入Coordinator和Participant来决定是否把事务真正的提交
## 2PC (two phase commit)
+ 阶段一: 提交事务请求
    - 事务询问： 协调者向所有参与者发送事务内容，询问是否可以执行事务提交操作，并等待参与者的响应
    - 执行事务: 各参与者执行事务操作，并将Undo和Redo操作记录到事务中
    - 参与者反馈事务询问的响应: 
+ 阶段二: 执行事务提交
    - 发送提交请求: 向所有参与者节点发出Commit请求
    - 事务提交:参与者接收到commit请求后，会正式执行事务提交操作，并在提交完成后释放整个事务执行期间所占用的事务资源
    - 反馈事务提交结果: 参与者在完成事务提交后，想协调者发送ack确认消息
    - 完成事务:协调者接收到所有参与者反馈的ack确认消息，完成事务
+ 优点: 简单   
+ 存在的问题: 同步阻塞、单点问题、脑裂、太过保守    
    - 同步阻塞: 二阶段执行过程中，所有的参与者都处于阻塞的状态，已完成的参与者需要等待其他参与者的提交完成
    - 单点问题: 协调者出现问题导致参与者尚未完成的都会出现问题
    - 数据不一致性: 因为网络问题Commit部分机器没有收到消息导致数据不一致
    - 太过保守: 任意一个节点出错，整个分布式系统都会出错
## 3PC
+ 阶段一:CanCommit
    - 事务询问
    - 各参与者反馈是否可以提交事务
+ 阶段二: PreCommit(正常执行事务提交、中断事务)
    - 发送预提交请求
    - 事务预提交
    - 各参与者想协调者反馈事务执行的响应
    - 中断事务: 发送中断请求，参与者收到abort指令或者等待指令的时间内超时直接中断事务
+ 阶段三
    - 发送提交请求
    - 事务提交
    - 反馈事务完成结果
    - 完成事务
    - 中断事务: 发送中断请求，参与者收到abort指令或者等待指令的时间内超时直接中断事务
+ 优点: 相对于2PC来说，降低了参与者的阻塞范围，并且能够在出现单点故障之后继续保持一致性  
存在的问题:在preCommit阶段因为网络问题还是可能导致数据的不一致性

# 第三章 Paxos的工程实践
## Chubby
是一个分布式锁服务，Chubby



# Zookeeper基础
+ 协调：多个节点完成一个动作
+ 数据模型: 分层结构，树形结构中的每个节点叫做Znode,每个Znode都有数据(byte[]类型)，也可以有节点
    - 节点路径: 斜线分割: /Zoo/Duck
    - 没有相对路径
+ 通过数据结构stat来存储数据的变化，acl的变化和时间戳
+ 数据发生变化时，版本号会发生变化
+ 可以对Znode中的数据进行读写操作


## Zookeeper的应用场景
+ 数据发布/订阅(数据中心)
    - 发布者将数据发布到zk的一个或者一系列的节点上，订阅者进行数据订阅，当数据发生变化的时候，可以及时得到数据的变化通知
+ 负载均衡
    - 本质是利用zookeeper的配置管理功能，涉及到的步骤:
    - 1. 服务提供者把自己的域名ip端口注册到zk上
    - 2. 服务消费者通过域名从zk中获取到对应的ip及端口，这个ip端口有多个，只获取其中一个
    - 3. 当服务提供者宕机了，对应的域名与ip的对应就会减少一个映射
    - 4. 阿里的dubbo服务框架就是基于zk来实现服务路由和负载
+ 命名服务(类似jndi)
+ 分布式协调/通知
    - 通过watcher的通知机制实现
    - 分布式锁
    - 分布式事务
+ 集群管理
    - 当前机器中的机器数量
    - 集群中机器的运行状态
    - 集群中节点的上下线操作
    - 集群节点的统一配置
+ master选举
    - 临时节点
    - 顺序节点
+ 分布式锁
    - 排他锁
    - 共享锁
+ 分布式队列
    - FIFO
+ 集群角色
     - leader: 为客户端提供读和写的操作
     - follower: 提供读服务
     - observer: 提供读服务，不参与选举，一般是增强集群的读请求并发能力
+ 会话
    - zk的客户端和服务端之间的连接
    - 通过心跳检测保持客户端连接的存活
    - 接收来自服务端的watch事件通知
    - 可以设置超时时间
+ 版本
    - Version: 当前zNode的版本
    - Cversion: 当前zNode的子节点版本
    - Aversion: 当前zNode的ACL(访问控制)版本
+ watcher
    - 作用域zNode节点上
    - 多种事件通知: 数据更新、子节点状态
+ ACL(access control lists)
    - 类似于linux的权限控制
    - create: 创建子节点权限
    - read: 获取节点数据和子节点列表的权限
    - write: 更新节点数据的权限
    - delete: 删除子节点的权限
    - admin: 设置节点acl的权限

## zk的单机模式
### zkCli.cmd
+ 不带任何参数连接到localhost:2181 
+ zkCli.cmd -server localhost:2181

### ls
- ls path [watch]n
    - path表示指定数据节点的节点路径
    - 列出指定节点下的所有子节点
    - 只能查看第一级的所有子节点
    - 刚安装时 `ls / `下只有默认的zookeeper保留节点
    - watch表示监听path的子节点的变化
### create
- create [-s] [-e] path data acl
    - -s或者-e表示创建的是顺序或者临时节点，不加默认创建的是持久节点
    - path为节点的全路径，没有相对节点的表示方式
    - data为当前节点内存储的数据
    - acl用来进行权限控制，缺省情况不做任何权限控制
### get
- get path [watch]
    - 获取指定的数据内容和属性信息
    - Path表示指定数据节点的节点路径
### set
- set path data [version]
    - 更新指定节点的内容数据
    - version为指定被更新的数据版本，一般不指定，如果数据版本已经更新，则指定旧版本时会报错
### delete
- delete path [version]
    - 删除指定节点，path表示被删除的节点
    - version作为乐观锁控制
### Znode
+ 数据节点(Znode)
    - 不是机器的意思
    - zk树形结构中的数据节点，用于存储数据
    - 持久节点: 一旦创建，除非主动调用删除操作，否则一直存储在zk上
    - 临时节点: 与客户端的分享绑定，一旦客户端会话失效，这个客户端所有的创建节点都会被移除
    - SEQUENTIAL Znode: 创建节点时，如果设置属性SEQUENTIAL,则会自动在节点名后追加一个整型数字
+ 临时节点: 关闭会话时消失，创建的时候加上-e
+ watch
    - zk中引入watcher机制来实现发布订阅功能，能够让多个订阅者同时监听某一个主题对象
    - watcher设置后一旦触发一次就会失效，如果需要一直监听，就需要注册
+ 客户端watch注册流程
    - 调用客户端api，传入watcher对象
    - 标记request，封装watcher到watcherRegistration
    - 向服务端发送request
        - 响应成功: 将watcher注册到ZkWatchManager进行管理
        - 响应失败: 请求返回(请求结束)
     - request -->(processRequest) FinalRequestProcessor -->(getData)  ZKDatabase --> DataTree -->(addWatcher) WatchManager
     - ServerCnxn类： 表示客户端与服务端的tcp连接，实现了watcher接口
     - watchManager 类: zk服务端watcher的管理者，watchTable: 从数据节点的粒度来维护  watch2Paths从watch粒度来维护，负责watcher的触发
     - DataTree 类： 维护节点目录树的数据结构
+ 客户端回调watcher步骤
    - 反序列化将字节流转换成watcherEvent对象
    - 处理chrootPath
    - 还原watcherEvent: 把watcherEvent对象转换成watchedEvent
    - 回调watcher: watchedEvent对象交给EventThread对象
+ EventThread: 从ZKWatcher中取出watcher，并放入waitingEvents队列中

### ACL(access control list)
+ scheme:id:permission 比如 world:anyone:crdwa
+ scheme: 验证过程中使用的校验策略
    - world: id固定是anyone表示任何人，world:anyone:crdwa表示任何人都具有crdwa权限
    - auth: 给认证通过的所有用户设置acl权限，可以添加多个用户
        - addauth digest {username}:{password}
        - auth策略本质上是digest,addauth创建多组用户和密码，当使用setAcl修改权限时，所有的用户和密码的权限都会被修改
        - 通过addauth新创建的用户和密码组需要重新调用setAcl才会加入到权限中
   - digest: 指定密码访问，不是所有的
   - ip： 指定ip访问
   - super： 有权限操作任何节点
+ id：权限被赋予的对象，比如ip或者某个用户
+ permission: 权限，crdwa表示五个权限的组合
+ 通过setAcl命令设置节点的权限
+ 节点的acl不具有继承关系
+ getAcl可以查看节点的acl信息


## Java客户端
### create session
```java
/**
 * 客户端和服务端会话的建立是一个异步的过程
 *  完成客户端的会话就返回，但是此时连接还没有真正的建立起来
 *  当真正连接建立起来的时候会在watcher会接收到一个通知
 *  connectionString: 想要连接的机器，多个用逗号分开,也可以在ip后面直接跟上操作的目录
 *  sessionTimeout: 会话超时时间，单位是毫秒，这个时间内没有收到心跳检测会话就会失效
 *  watcher: 注册的watcher,不设置则使用默认的watcher
 *  canBeReadOnly: 当前会话是否支持"read-only"模式 , 当zk集群中某个机器与过半以上机器断开后，此机器将不会再接受客户端的任何读请求，但是有时候希望继续接受读请求可以使用该参数
 *  sessionId: 会话id
 *  password: 可以通过sessionId和password实现会话复用
 */
```
### create node
```java
/**
 * 创建节点同步创建和异步创建，都不支持递归创建
 *      当节点存在的时候抛出异常
 *  path: 被创建的节点路径
 *  data: 字节数组
 *  acl: acl策略
 *  createMode: 节点类型，主要包含： 持久、持久顺序、临时、临时顺序
 *  cb: 异步回调函数，需要实现StringCallback接口，当服务器创建成功会自动调用它的 processResult 方法
 *  ctx: 传递上下文信息或者添加自定义参数
 */
```

### delete node
```java
/**
 * 删除节点
 * path: 被删除节点的路径
 * version: 数据节点的版本，如果不是最新版本将会报错，类似乐观锁
 * cb: 异步回调函数
 * ctx: 传递上下文对象
 * 异步删除的时候主线程不能提前结束 
 */
```

### get child node
```java
/**
 * 获取子节点 getChildren()
 * path: 指定路径的子节点
 * watch: 是否使用默认的watcher
 * cb: 回调函数
 * ctx: 上下文信息
 * stat: 指定数据节点的状态信息
 */
```
### get child node data
```java
/**
 * getData
 * path: 需要获取数据的路径
 * watcher: 设置watcher后发生数据变化时会受到通知
 * watch: 是否使用默认watcher
 * stat: 指定数据节点状态信息
 * cb: 回调函数
 * ctx: 上下文信息
 */
```
## zookeeper集群
+ 是一种对等集群，所有的节点的数据都一样
+ 集群之间依靠心跳来感知彼此的存在
+ 所有的写操作都在主节点，其他节点只能读，虽然可以接受写请求，但是会把写请求转发给主节点
+ 通过选举机制选出主节点，从而保证主节点的高可用
+ 至少三个节点，必须是奇数个节点
+ 当一半以上的节点数据写入成功后，则返回成功，是最终一致性策略
### 数据一致性
+ 数据复制
    - 单节点写入再复制到其他节点，zookeeper的实现方式
    - 多节点同时写入，数据没有相关性  tomcat的实现方式
+ 集中存储
    - redis
## 开源客户端
+ 原生api的不足
    - 连接的创建是异步的，需要编码人员自行编码实现等待
    - 连接没有自动超时重连机制
    - zk本身不提供序列化机制，需要开发人员自行指定，从而实现数据的序列化和反序列化
    - watcher注册一次只会失效一次，需要不断的重复注册
    - 不支持递归创建树形节点
+ zkClient
    - 解决了超时重连、反复注册、简化开发api
+ curator
    - 解决session会话超时重连
    - watcher反复注册
    - 简化开发api
    - 遵循Fluent风格api
    - 共享锁、master选举、分布式计数器
```java
/**
 * connectionString: 连接字符串信息
 * sessionTimeoutMs: 会话超时时间,默认6s
 * connectionTimeoutMs: 创建连接超时时间 ，默认15s
 * retryPolicy: 重试策略,官方实现有三种
 *      RetryNTimes     (int n, int sleepMsBetweenRetries)  最大重试次数,每次重试间隔时间
 *      RetryOneTime     上面n=1的情况
 *      RetryUntilElapsed  重试的时间超过最大时间后，就不再尝试
 */
```