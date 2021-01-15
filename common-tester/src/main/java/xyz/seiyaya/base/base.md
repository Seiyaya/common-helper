+ String,StringBuilder,StringBuffer区别
# String
    - String 声明的是不可变的对象，每次操作都会生成新的 String 对象，然后将指针指向新的 String 对象。都是被final修饰过了的
    - StringBuffer的操作字符串的方法都是被synchronized同步关键字包装过

# List
## ArrayList
扩容: size * 3/2 +1,jdk1.8进行的是 **size + (size >> 1)** ,最终都是调用**Arrays.copyOf**方法  

删除: **System.arraycopy**方法整体向前移动一个位置，被删除的位置为null  

添加: 添加位置之后的向后移动(判断是否需要扩容)，添加位置插入元素
vector和arraylist的区别: Vector是线程安全的，ArrayList是线程非安全的,vector指定了增长因子，扩容的时候加上这个数  
arraylist的elementData用transient修饰
    - 因为在序列化的过程中ArrayList的elementData不一定是满的，所以ArrayList重写了**writeObject**方法
    
## LinkedList

## CopyOnWriteArrayList
+ vector虽然是线程安全的，但是它针对的是单个操作的线程安全，几个操作组合在一起仍然是线程不安全的，同时增删也会抛出**ConcurrentModificationException**
+ CopyOnWriteArrayList的理念
    - 读写分离
    - 最终一致性: 能够容忍一定的时间内的数据不一致性，不是任何场景都实用(比如火车售票，秒杀)
    
# Map
## HashMap
HashMap的table为什么是transient   
    因为hashCode方法和平台有关，不同的VM平台hashCode不一样，解决办法是重写**writeObject**方法

# Object
+ 对象的深浅复制(深浅克隆)
    - 实现克隆的方法，实现接口`Cloneable`接口，然后实现方法clone，可以实现深克隆
    - 浅克隆: 复制属性的时候只复制它本身和包含的值类型变量，引用类型的只会复制对应的引用，不产生新的对象
    - 深克隆: 引用类型的属性也会进行一次复制，一直递归到都是值类型属性

# ThreadLocal
+ 多线程环境可以避免线程自己的变量被其它线程篡改
+ 本质上是以threadLocal为key，存储的变量为value作为一个Entry存储在当前线程的 threadLocals 变量上
+ 不同于HashMap解决hash冲突的方法，采用的是冲突时取下一个索引的方式


# 线程
## stop() 和 destroy()
+ 能否将运行一半的线程直接杀死
    - 理论上可以但是并不推荐，因为线程使用的资源(文件描述符、网络链接)等不能正常关闭
    - 最好是不要强行打断线程，合理的方法是等待线程运行完毕
## 守护线程
+ 子线程并不会因为主线程的结束而停止运行，只有在jvm停止时才会停止，也就是说守护线程不会影响到JVM的退出，而所有的非守护线程退出才会导致JVM退出
## InterruptedException
+ 只有那些声明了会抛出 InterruptedException 的函数才会抛出异常，sleep、wait、join
+ 轻量级阻塞和重量级阻塞
    - 轻量级阻塞: 能够被中断的阻塞，对应的线程状态是 WAITING 和 TIMED_WAITING
    - 重量级阻塞: 不能被中断，比如 synchronized 关键字，对应状态是 BLOCKED
    - t.interrupted()方法的意思是唤醒轻量级阻塞

## Synchronized
+ 锁的本质
    - 锁必须拥有标志位(state变量)
    - 锁如果被占用需要记录当前是被哪个线程占用
    - 锁还需要维护一个 list 来记录等待阻塞的线程
+ synchronized的实现原理
    - java对象头里，有一块数据叫 Mark Word ，在64位机器上，它是8字节的，有两个重要的字段: 锁标志位和持有该锁的threadId

## notify 和 wait
+ 生产者消费者模型
    - 内存队列本身需要加锁，才能实现线程安全
    - 阻塞，当内存队列满时生产者放不进去会被阻塞，反之当内存队列为空时消费者没有消费会被阻塞。可以利用wait notify方法或者使用阻塞队列(BlockingQueue)
    - 双向通知，消费者阻塞之后又添加了数据需要notify消费者，反之消费者消费之后需要通知生产者。可以使用 notify 和wait方法进行通知，或者利用Condition
    
+ 必须和synchronized方法一起使用
    - 线程之间需要通信，一个线程调用wait方法，一个线程调用notify方法，该对象本身就需要同步
+ wait的时候必须释放锁
    - 线程A如果不释放锁，它将直接阻塞，导致锁不能正常释放，线程B调用notify之前需要获取锁，但是线程A没有释放导致死锁

## volatile
+ 64位写入的原子性
+ 内存可见性: A线程修改的内容，对B线程来说不是立即可见
+ 禁止重排序: 执行的顺序不一致都导致的错误
## JMM 和 happen-before
+ cpu缓存一致性问题
    - 多个CPU之间的缓存不会出现不同步的问题(CPU缓存一致性)，也就是l1、l2之间不会存在不同步的问题
    - 但是同步缓存存在性能消耗，在计算单元和l1之间增加了 store buffer 和 load buffer。这些buffer和l1之间的同步是异步的
+ 重排序和内存可见性的关系
    - store buffer延迟写入是重排序的一种，称为内存重排序，还有CPU指令重排序和编译器的重排序
    - CPU内存重排序: CPU有自己的缓存，指令的顺序和写入内存的顺序不一致
    - CPU指令重排序: 在指令级别，让没有依赖关系的多条指令并行
    - 编译器重排序: 对于没有依赖关系的语句，编译器可以重新调整语句的执行顺序
+ as-if-serial
    - 每个线程对外的表现可以认为是完全串行的
+ 内存屏障
    - 为了禁止 编译器重排序 和 CPU重排序 ，在编译器和CPU层面都有对应的指令。这也是JMM和happen-before规则的底层实现原理
+ JDK中的内存屏障
    - unsafe类提供了三个内存屏障函数

## 无锁编程
+ 一写一读的无锁队列: 内存屏障
    - linux内核的kfifo队列，一写一读两个线程，不需要锁，只需要内存屏障
+ 一写多读的无锁队列: volatile关键字
    - Disruptor能够在无锁的情况下实现Queue的并发操作
    - RingBuffer能够无锁也是因为单线程写，离开这个前提没有任何技术可以做到完全无锁
    - 在单线程写的前提下，利用volatile关键字可以实现一写多读的线程安全。
    - 具体就是 RingBuffer 有一个头指针，对应一个生产者线程，多个尾指针对应多个消费者线程，每个指针都是volatile类型的变量
    - 每个消费者只会操作自己的尾指针，通过头指针和尾指针比较判断是否队列为空
+ 多写多读的无锁队列:CAS
    - 同内存屏障一样，CAS是CPU提供的一种原子指令
    - 基于CAS和链表可以实现一个多写多读的队列，入队和出队都通过CAS完成
+ 无锁栈
    - 只需要head指针进行CAS操作，能够实现多线程的入栈和出栈
+ 无锁链表
    - ConcurrentSkipListMap 
    
# Atomic类
## AtomicInteger和AtomicLong
+ 乐观锁与悲观锁
+ unsafe的cas操作(compare and swap)
    - unsafe.compareAndSwapInt(obj,valueOffset,expect,update)
    - 第二个参数是long类型的，表示的是某个成员变量在对应类中的内存偏移量，也就是成员变量本身(非引用)  
    - unsafe.objectFieldOffset(Field) 成员变量转成对应的内存偏移量
+ 自旋与阻塞
    - 放弃CPU进入阻塞状态，等待后序被唤醒
    - 不放弃CPU，空转，不断重试
    - 单核CPU只适合第一种，多核CPU适合第二种，没有了线程上下文切换的开销。AtomicInteger的实现方式是自旋，如果拿不到锁就会一直重试
## AtomicBoolean和AtomicReference
+ AtomicBoolean的支持是依赖AtomicInteger  将boolean类型的转换为int类型进行操作

## AtomicStampedReference 和 AtomicMarkableReference
+ CAS直接比较值存在ABA问题，解决方法使用 AtomicStampedReference 引入版本号解决
+ 前者是用int来表示版本号，后者是用boolean类型的变量来表示版本号(并不能完全解决ABA问题)
+ 两者内部都会将值和版本号封装成 Pair， 详见: @see： java.util.concurrent.atomic.AtomicStampedReference.Pair

## AtomicIntegerFieldUpdater
+ 对已有的类进行原子操作，不需要修改源代码
+ 需要下的变量必须用volatile的int类型

## AtomicIntegerArray
+ 数组元素进行原子操作

## Striped64 和 LongAdder
+ jdk8新增对long的原子操作
+ LongAddr主要运用分片的思想，类似ConcurrentHashMap的分段锁。一个base变量和多个子Cell并发度高的情况分摊到子Cell,最后合并
+ 没有直接的AtomicDouble类，都是double和long相互转换进行操作
+ 只具有最终一致性，而不具有强一致性
+ 伪共享与缓存行填充
    - JDK8新增注解 @Contended 
    - CPU缓存与主内存进行数据交换的基本单位是 Cache Line, 如果多个变量在同一个cache line中，改变一个三个变量都要刷新到主内存
    
# Lock与Condition
## 互斥锁
+ 可重入
+ 实现原理都是基于AQS实现(队列同步器)
    - 需要一个state变量来标记锁的状态，至少有两个值(0,1),要确保线程安全，也就会用到CAS
    - 需要记录当前哪个线程持有锁
    - 需要底层支持对一个线程进行阻塞和唤醒操作(LockSupport进行操作,notify无法唤醒具体的线程)
    - 需要一个队列维护所有阻塞的线程，这个队列也必须是线程安全的无锁队列，也需要用到CAS(双向链表和CAS实现阻塞队列)
+ 非公平锁(NonfairSync)
    - 直接尝试修改state变量的值为1，成功直接设置当前线程持有，修改失败再进行尝试申请排队 acquire(1)
    - acquire
        - tryAcquire： state=0再次尝试去修改state的值,否则当前线程是否是重入，重入state+1
        - addWaiter: 线程生成node放到双向队列的尾部(线程并未阻塞)，CAS放入尾部失败执行enq()方法
        - enq: 执行队列的初始化或者一直CAS尝试新节点放到队列尾部
        - acquireQueued: 找到头指针所在节点，头节点尝试获取锁，成功则将头结点的下个节点设置为头结点，其他线程调用t.interrupt()返回true,其他线程调用unpark返回false
        - selfInterrupt: 根据 acquireQueued 的条件判断是否打断当前线程
     - unlock
        - tryRelease释放锁，不需要CAS操作，因为是排他锁，持有锁的线程只有一个
        - unparkSuccessor唤醒队列的后继者
+ Node的waitStatus属性: 决定对应节点在当前情况下处于何种状态
    - CANCELLED(1): 取消状态，等待超时或者中断了，这时需要将其从等待队列中删除，进入到该状态的节点也将不再变化，作为结束状态
    - SIGNAL(-1): 等待触发状态，当前线程需要阻塞，等待前置节点释放锁之后唤醒当前节点
    - CONDITION(-2): 等待条件状态，当前节点再等待condition,即在condition队列中，当调用 Condition#signal 将状态变为等待同步锁
    - PROPAGATE(-3): 状态需要向后传播，表示releaseShared需要传播给后序节点，尽在共享锁模式下使用
+ Node的 nextWaiter 属性
    - AQS中条件队列是使用单向列表保存的，用 nextWaiter来连接
    - nextWaiter实际上标记的就是在该节点唤醒后依据该节点的状态判断是否依据条件唤醒下一个节点
+ 公平锁(FairSync)
    - 调用 acquire(1) 排队申请
## 读写锁
```java
ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
```
+ 读线程和读线程之间不用互斥
+ 读写锁看起来是两个，但其实都是同一把锁的两个视图，使用一个int变量来表示读锁和写锁的重入次数。CAS只能修改一个变量
+ 读锁使用的是共享锁，写锁使用的是排他锁(上面的ReentrantLock)

## Condition
+ Condition是一个接口，功能和notify/wait方法类似
+ ArrayBlockQueue就使用了Condition来做消费和生产的信号量(核心是一把锁+两个Condition)
+ 每个Condition上都阻塞多个线程，所以在实现ConditionObject内部有一个双向链表组织的队列
+ await()
```java
// 1.如果当前线程收到中断的信号抛出异常
if (Thread.interrupted())
    throw new InterruptedException();
// 2.加入Condition等待队列 @see: java.util.concurrent.locks.AbstractQueuedSynchronizer.ConditionObject.await()
Node node = addConditionWaiter();
// 3. 阻塞之前必须释放锁，否则会发生死锁
int savedState = fullyRelease(node);
int interruptMode = 0;
while (!isOnSyncQueue(node)) {
// 4.自己阻塞自己
    LockSupport.park(this);
    if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
        break;
}
// 5.重新拿锁
if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
    interruptMode = REINTERRUPT;
if (node.nextWaiter != null) // clean up if cancelled
    unlinkCancelledWaiters();
if (interruptMode != 0)
// 6.被中断唤醒，向外抛出中断异常
    reportInterruptAfterWait(interruptMode);
```
+ awaitUninterruptibly()
    - 不会响应中断，函数定义没有中断异常抛出，和await的区别就是收到中断信号的时候继续执行循环
+ notify()
    - 只有持有锁的线程才有资格调用 signal()
    - 唤醒队列中的第一个线程
## StampedLock
+ 与读写锁的区别是读读、读写不互斥,可以认为是乐观读,实现原理类似MySQL的MVCC机制，一份数据多个版本
+ 读的时候不加锁，读出来之后发现数据被修改了，再升级为悲观锁
+ @see xyz.seiyaya.base.StampedLockDemo
+ 乐观锁原理
    - 采用和读写锁一样的都使用一个变量表示读锁的数量和写锁的数量，另外需要版本号也使用该变量

# 同步工具类
## Semaphore
+ 信号量，提供了资源数量的并发访问控制
## CountDownLatch
+ 场景: 一个主线程要等待10个Work线程执行完才推出
+ await() 和  countDown()
## CyclicBarrier
## Exchanger
## Phaser
+ CountDownLatch 和 CyclicBarrieer的增强版

# 并发容器
## BlockingQueue
+ ArrayBlockingQueue
    - 数组实现的环形队列
```java
// 构造函数
public ArrayBlockingQueue(int capacity, boolean fair){}

/* add offer put 
add 队列满时抛出异常
offer 队列满时返回false
put 没有返回值，会抛出中断异常

remove peek take
remove和peek是非阻塞式的
take是阻塞式的
*/  

// 核心是 排他锁 + 2个condition
lock = new ReentrantLock(fair);
notEmpty = lock.newCondition();
notFull =  lock.newCondition();
```

+ LinkedBlockQueue
    - 基于单项链表的阻塞队列，拥有队头和队尾两个指针，所以用了2个锁+2个条件+AtomicInteger记录元素数量
    - 两个锁的结果是put与take之间不互斥，同种操作互斥，但是count数量必须为原子类型，两边都会操作

+ PriorityBlockingQueue
    - 按照优先级的从小到大出队
    - 数组实现的二叉小根堆，1把锁+1个条件没有非满的条件(超出限制进行扩容)，默认的大小是11
+ DelayQueue
    - 按照延迟时间从小到大的出队
+ SynchronousQueue
    - 特殊的BlockingQueue,本身没有容量，调用put线程会阻塞，直到另外一个线程调用了take(),两个线程才同时解锁
    - 线程池CachedThreadPool 的实现就用到了 SynchronousQueue 
## BlockDequeue
+ LinkedBlockDequeue
    - 阻塞的双端队列,实现为1把锁+2个条件，与LinkedBlockingQueue 实现不同的是它采用的是双向链表
## CopyOnWrite
+ 写的时候不是直接向数据源写数据，而是把数据拷贝一份进行修改，再通过悲观锁或者乐观锁的方式进行回写
+ CopyOnWriteArrayList
    - 数据结构是一个数组，读相关的函数都没有做额外的锁处理
    - 写数据的时候会进行悲观锁处理,把新的数组赋值给旧的数组
+ CopyOnWriteArraySet
    - 内部维护的仍然是一个CopyOnWriteArrayList，但是元素添加的时候会去判断是否重复
## ConcurrentLinkedQueue
+ 和AQS中的队列原理基本一致，头尾都是基于CAS进行出队和入队，区别在于 ConcurrentLinkedQueue 是单向链表
    - 初始化，tail 和 head 都指向一个空节点
## ConcurrentHashMap
+ jdk1.7的实现方式
    - 一个hashmap被拆分成多个hashmap,每个子hashmap被称为Segment。多个线程操作多个Segment相互独立
+ jdk1.7实现的好处
    - 减少hash冲突，避免一个槽内太多元素
    - 提高读和写的并发度，段与段之间隔离
    - 提高扩容的并发度，扩容的时候不是整个ConcurrentHashMap扩容，而是Segment进行扩容
+ jdk1.8的实现方式
    - 去除了JDK1.7的分段锁，数组中的节点可能是 Node 或者 TreeNode
+ jdk1.8实现的好处
    - 使用红黑树当一个槽内有很多元素的时候，查询更新速度快，避免大量hash冲突的问题
    - 加锁的粒度不是对整个Map进行加锁，对数组的每个头结点进行加锁
    - 并发扩容，
## ConcurrentSkipListMap
+ 基于红黑树实现的Map --> TreeMap , concurrent包中提供key有序的HashMap --> ConcurrentSkipListMap
+ 没有使用红黑树，而是选用SkipList是实现有序性。(原因是无锁化编程作用在树上效果不及list)，ConcurrentHashMap使用红黑树也是使用悲观锁控制修改对应位置的Node
+ 无锁链表
    - 因为不是对头和尾进行添加删除进行CAS操作，对中间的元素也可以操作，就会导致线程安全问题。比如添加一个节点和删除一个节点，添加之后，再删除，导致新的节点也删掉了
    - 解决上面的问题必须将 插入一个新节点 和 判断上一个节点是否删除 作为一个原子操作进行CAS
+ 跳查表
    - 跳查表的本质是多层链表叠加起来
    - 查找的顺序是从左到右，从上到下
```java
// 存储真实的数据，作为SkipList的最底层
static final class Node{
    final K key;
    volatile Object value;
    volatile Node<K,V> next;
}
// index层节点
static final class Index{
    // 不存储实际数据，指向Node
    final Node<K,V> node;
    // 每个Index节点，必须有个指针指向下一个level对应的节点
    final Index<K,V> down;
    // 自身组成单向链表，同一个层级
    volatile Index<K,V> right;
}

class ConcurrentSkipList extends AbstractMap{
    // 指向顶层index的head节点
    private transient volatile HeadIndex<K,V> head;
}
```
# 线程池与Future
## 实现原理
+ 调用方提交任务到线程池的队列，线程池的线程消费队列中的任务
+ 线程池需要考虑的问题
    - 队列有界无界，调用方不断往队列中放任务，可能导致内存耗尽，有界针对多余的任务的处理
    - 线程池的线程数量动态还是静态
    - 每次提交任务是放入队列还是开线程
    - 当没有任务的时候线程是休眠还是阻塞，阻塞的话是谁唤醒
+ 没有任务的时候线程池策略
    - 不使用阻塞队列，只是用一般的线程安全的队列，也没有阻塞-唤醒机制，通过休眠来等待新的任务
    - 不使用阻塞队列，在队列的外部、线程池内部使用阻塞-唤醒机制
    - 使用阻塞队列
## ThreadPoolExecutor
```java
// 状态变量
final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
// 存放任务的阻塞队列
final BlockingQueue<Runnable> workQueue;
// 对线程池内部各种变量进行互斥访问控制
final ReentrantLock mainLock = new ReentrantLock();
// 线程集合，每个线程是一个Worker对象
final HashSet<Worker> workers = new HashSet<Worker>();

// 继承aqs，本身就是一把锁
class Worker extends AbstractQueuedSynchronizer implements Runnable{
    // 封装成worker的线程
    final Thread thread;
    // worker接收到的第一个任务
    Runnable firstTask;
    // worker执行完毕的任务个数
    volatile long completedTasks;
}
```
+ 核心配置参数解释
    - corePoolSize: 在线程池中始终维护的线程个数
    - maxPoolSize: 在corePooSize已满、队列也满的情况下，扩充线程至此值
    - keepAliveTime: maxPoolSize 中的空闲线程，销毁所需要的时间，总线程数收缩回corePoolSize
    - blockingQueue: 线程池所用的队列类型
    - threadFactory: 线程创建工厂，可以自定义，也有一个默认的
    - RejectExecutionHandler: corePoolSize 已满，队列已满，maxPoolSize 已满，最后的拒绝策略
    - 新建的任务会优先交给新建的core线程执行，只有当线程数 > corePoolSize,则添加到队列
+ 线程池的关闭
    - 线程数量和线程状态在jdk1.7是分开存储，即 AtomicInteger的ctl，最高的三位表示线程池状态，其余29位表示线程数量 ，1.6是分开存储
    - 线程池状态: 
        - RUNNING(-1) 
        - SHUTDOWN(0执行了shutdown方法)
        - STOP(1执行了shutdownNow方法)
        - TIDYING(2队列和线程池为空)
        - TERMINATED(3执行terminated方法,被动执行)，只要是TIDYING状态就会执行terminated方法
        - 只会从小到大迁移，不会逆过程
+ 正确关闭线程池的步骤
    - 调用shutdown()或者shutdownNow()不会立刻关闭，接下来需要调用 awaitTermination 来等待线程池关闭
    - awaitTermination 方法不断循环判断线程池是否到达了最终状态 TERMINATED
+ shutdown 与 shutdownNow 的区别
    - 前者不会清空任务队里，会等所有任务执行完成，后者会请清空任务队列
    - 前者只会中断空闲的线程，后者会中断所有线程
+ 线程池的四种拒绝策略
    - AbortPolicy: 线程池直接抛出异常，默认策略
    - CallerRunsPolicy: 让调用者直接在自己的线程里执行，线程池不做处理
    - DiscardPolicy: 线程池直接把任务丢弃
    - DiscardOldestPolicy: 将队列里面最老的任务删除掉，把该任务放入队列

线程池类型
    + newFixedThreadPool  -->  LinkedBlockingQueue
    + newScheduledThreadPool  --> DelayQueue
    + newCachedThreadPool --> SynchronousQueue
## Callable与Future
```java
// 具有返回值的Runnable
public interface Callable<V>{
    V call() throws Exception;
}

// submit最终也是将callable通过适配器转换成Runnable
Future<String> f = executor.submit(callable);
// 没有执行完就阻塞在这里
String result = f.get();

// jdk1.6借用AQS的功能实现阻塞和唤醒   1.7 使用CAS+state+park/unpark实现阻塞与唤醒
```
## ScheduledThreadPoolExecutor
+ 实现了按照时间调度来执行任务
    - 延迟执行任务
    - 周期执行任务
        - scheduleAtFixRate: 与本身执行时间无关，间隔5s就是开始的下5s执行下一次
        - scheduleWithFixedDelay: 与本身执行时间有关，执行时间+间隔下次的时间为下一次的执行时间
+ 延迟执行任务原理
    - 没有使用 DelayQueue，而是使用ScheduledThreadPoolExecutor内部又实现的一个特定DelayQueue
    
# logback
## logback解决的问题
1. 内核重写、充分测试、初始化内存加载更小
2. logback实现了slf4j
3. 文档齐全
4. logback当配置文件修改了，支持自动重新加载配置文件，扫描过程快且安全，它并不需要另外创建一个扫描线程

## logback加载
1. 在系统配置文件System Properties中寻找是否有``logback.configurationFile``对应的value
2. 在classpath下寻找是否有logback.groovy（即logback支持groovy与xml两种配置方式）
3. 在classpath下寻找是否有logback-test.xml
4. 在classpath下寻找是否有logback.xml
5. 具体实现代码参考: ``ch.qos.logback.classic.util.ContextInitializer#findURLOfDefaultConfigurationFile``


## logback的configuration
+ ``<configuration>``
    - scan: 当scan被设置为true时，当配置文件发生改变，将会被重新加载，默认为true
    - scanPeriod: 检测配置文件是否有修改的时间间隔，如果没有给出时间单位，默认为毫秒，当scan=true时这个值生效，默认时间间隔为1分钟
    - debug: 当被设置为true时，将打印出logback内部日志信息，实时查看logback运行信息，默认为false
+ ``<logger>``
    - 用来设置某一个包或者具体某一个类的日志打印级别,以及指定子节点 **appender**，**logger**可以包含0个或多个**appender-ref**元素
    - name属性: 用来指定受此logger约束的某一个包或者具体的某一个类
    - level属性: 用来指定日志打印级别，未设置会继承上层的日志级别
    - additivity属性: 是否向上级logger传递打印信息，默认为true
    - root节点是一个特殊的logger，name = root,代码参考 ``LoggerContext``
+ ``<appender>``
    - **<appender>** 是**<configuration>**的子节点，是负责**写日志**的组件，必要的属性name和class
    - name值的是appender的名称，class表示的是appender的全限定名
    - **<appender name="FILE" class="ch.qos.logback.core.FileAppender">**的子节点
        - **<file>** 表示写入的文件名
        - **<append>** 表示是否日志追加到末尾
        - **<encoder>** 输出格式
        - **<filter>** 当前日志级别下在进行一次过滤
    - class = ch.qos.logback.core.rolling.RollingFileAppender,滚动记录文件，先将日志输出到指定文件，符合指定条件再将日志记录到其他文件
+ ``AsyncAppender``
    1. 异步日志大致执行流程：配置了异步日志系统会初始化一个 **AsyncAppender-Worker-ASYNC** 的线程
    2. 当**Logging Event**进入 AsyncAppender后，AsyncAppender会调用**appender()**方法，appender方法中再将event填入Buffer前(使用的Buffer为BlockingQueue，具体实现为ArrayBlockingQueue)
        会先判断当前Buffer的容量以及丢弃日志特性是否开启，当消费能力不如生产能力时，AsyncAppender会将超出Buffer容量的Logging Event的级别进行丢弃，作为消费速度一旦跟不上生产速度导致Buffer溢出处理的一种方式。  
        上面的线程的作用，就是从Buffer中取出Event，交给对应的appender进行后面的日志推送  
    3. AsyncAppender并不处理日志，只是添加了一个缓冲区，并在内部创建一个工作线程从队列头部获取日志，之后将获取的日志循环记录到附加的其他appender上去。  
        AsyncAppender仅仅充当的是事件转发器，必须引用另外一个appender来做事
    4. 具体的代码参考 ** AsyncAppenderBase ** 