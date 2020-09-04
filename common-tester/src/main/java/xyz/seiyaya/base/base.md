+ String,StringBuilder,StringBuffer区别
# String
    - String 声明的是不可变的对象，每次操作都会生成新的 String 对象，然后将指针指向新的 String 对象。都是被final修饰过了的
    - StringBuffer的操作字符串的方法都是被synchronized同步关键字包装过

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
## stop() 和 destory()
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
+ 内存可见性
+ 禁止重排序
## JMM 和 happen-before
+ cpu缓存一致性问题
    - 多个CPU之间不会出现不同步的问题
+ 内存可见性的问题

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
+ 只具有最重一致性，而不是强一致性
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