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