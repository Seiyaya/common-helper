# 集合

## HashMap
底层数据结构就是数组+链表实现(jdk1.8重构成红黑树,链表长度大于8)  
1. 构造函数:含有三个关键属性`loadFactor` 、`threshold` 、`initialCapacity`  
loadFactor: 负载因子，主要的作用是决定是否扩容的一个参数，已存储元素数量/size > loadFactor则进行扩容,原因是尽量避免元素过多导致hash冲突  
threshold: 最大的可以存储的元素数量,数组的大小*loadFactor，数组的大小再初始化的时候会得到大于该值的最大的2次幂，是为了&运算计算位置时都以hashCode为准
2. get方法
先计算key的在数组中的位置，然后直接`tab[(n-1)&hash]`得到那个位置存储的元素,如果得到的元素key的hash和equals都相同就直接返回  
如果数组的到的节点有下一个节点需要判断节点的类型，如果是TreeNode需要根据树的遍历得到对应的value,否则则是进行链表遍历

3. put方法
> 先得到key的hashCode,key==null => code=0

4. resize方法
> 作为初始化和扩容的方法，如果原来已经将链表树化，扩容的时候应该需要将树重新转换成链表，再根据实际情况转成红黑树  


5. treeifyBin
> 如果链表的长度大于8，且table数组的容量大小>=64。则扩展为红黑树，树的节点实例为TreeNode  
为了解决hash冲突的问题应该先扩容，再根据链表长度来决定是否扩展为红黑树。重建树所带来的问题是用什么来比较大小构建树  
1). 比较hash大小，如果hash相同，继续向下比较  
2). 检测类接口是否实现了Comparable接口，如果实现则利用它来进行构建  
3). tieBreakOrder使用该方法决定树构建  

6. split 
> 红黑树的拆分，再进行扩容的时候需要对红黑树拆分重新进行映射

+ 问题1: HashMap结构
数组+链表。链表再长度，数组长度大于64且链表长度大于8就会将链表优化成红黑树  

+ 问题2: 什么对象能做为key
重写了equals和hashcode方法的对象能作为key,否则即使属性一样也是不同的对象(默认比较的是内存地址值)  

+ 问题3: HashMap、ConcurrentHashMap、HashTable比较

## ArrayList
> 1.构造方法:没什么特别的，一个可以指定大小，或者默认大小为一个空数组    
2.add: 默认初始化数组大小为10,在索引为size处添加元素   
3.remove: 移除元素会将后面的部分整体copyOf到前方,然后将最后一个赋值为null等待gc  
4.grow:扩容方法，这里很多地方都用到了Arrays.copyOf(elementData, newCapacity)这个方法来进行复制  
其实它并不是进行for循环的进行赋值，而是调用底层native方法进行内存复制，所以相对高效.扩容的大小是3/2   

## LinkedList
> 总体上采用双向链表的结构，构造函数也不需要大小的声明。  
1.add:添加元素都追加到尾部结点的后继结点上  
2.remove  

## LinkedHashMap

## TreeMap

## ConcurrentHashMap

## AQS(AbstractQueuedSynchronizer)
AQS解决的问题: 获取同步状态，FIFO队列同步、独占锁和共享锁的获取与释放。主要还是作为一个基础组件提供给上层的ReentrantLock、CountDownLatch等使用  
+ AQS是建立在CAS的基础上的，提供了两种锁，分别是独占锁和共享锁
    - 独占锁: 操作是一种独占操作，具体的实现比如ReentrantLock,它实现了独占锁的方法
    - 共享锁: 非独占操作，比如CountDownLatch和Semaphore

+ Node的waitStatus属性
    - CANCELLED(1): 取消状态，前置节点已经等待超时或者中断了，这时需要将其从等待队列中删除
    - SIGNAL(-1): 等待触发状态，当前线程需要阻塞
    - CONDITION(-2): 等待条件状态，当前节点再等待condition,即在condition队列中
    - PROPAGATE(-3): 状态需要向后传播，表示releaseShared需要传播给后序节点，尽在共享锁模式下使用
+ AQS的核心内容
    - 用 volatile 修饰的整数类型的 state 状态，用于表示同步状态，提供 getState 和 setState 来操作同步状态
    - 供了一个 FIFO 等待队列，实现线程间的竞争和等待，这是 AQS 的核心(head指向持有锁的线程的节点，其余线程被加入到队尾，tail始终指向最后一个节点)
    - AQS 内部提供了各种基于 CAS 原子操作方法，如 compareAndSetState 方法，并且提供了锁操作的acquire和release方法

+ 独占锁: 如果有线程获取到锁，其他线程只能是获取锁失败，然后进入等待队列中等待唤醒
    - 具体的尝试获取锁流程  
    ![aqs](https://www.seiyaya.xyz/images/notes/base/aqs.jpg "aqs") 
    - 具体的释放锁流程
```
// 重要的属性记录
class AbstractQueuedSynchronizer{
    // 等待队列的头节点 表示当前正在执行的节点
    private transient volatile Node head;
    // 等待队列的尾节点
    private transient volatile Node tail;
    // 同步状态 >0表示有锁，每次加锁即state++,每次释放锁即在state-- ,实际上是调用compareAndSetState来实现自增和自减少操作，保证原子性
    private volatile int state;

    static class Node{
        Thread thread;// 存储当前线程对象
        int waitStatus;    
    }
    
    // 尝试获取锁，这个方法需要实现类自己实现获取锁的逻辑，获取锁成功后则不执行后面加入等待队列的逻辑
    // addWaiter --> tryAcquire尝试获取锁失败后，则将当前线程封装成Node添加到队尾
        // java.util.concurrent.locks.AbstractQueuedSynchronizer.addWaiter   基于当前线程独占类型的节点，利用CAS操作，将节点加入到队尾
    // acquireQueued --> 封装后的Node如果它的前驱节点是头节点则会尝试获取锁称为头节点，失败的话就会挂起,即进入 shouldParkAfterFailedAcquire()
    // shouldParkAfterFailedAcquire 判断前驱节点的waitStatus,如果是SIGN则进行挂起，删除状态为 CANCELLED 的节点，也就是根据前驱节点判断当前节点是否可以挂起
    public final void acquire(int arg){
        if(!tryAcquire(arg) && acquireQueued(addWaiter(Node.EXCLUSIVE), arg)){
            selfInterrupt();
        }
    }

    // 释放锁主要是将头节点的后继节点唤醒，如果后继节点不符合喊醒条件，继续往队列后面查找
    public final boolean release(int arg) {
      if (tryRelease(arg)) {
        Node h = head;
        if (h != null && h.waitStatus != 0)
          unparkSuccessor(h);
        return true;
      }
      return false;
    }
}
```

+ 共享锁![aqs](https://www.seiyaya.xyz/images/notes/base/aqs_2.jpg "aqs") 
```
public final void acquireShared(int arg) {
  // 尝试获取共享锁，小于0表示获取失败 , tryAcquireShared 主要是留给子类实现
  if (tryAcquireShared(arg) < 0)
    // 执行获取锁失败的逻辑
    doAcquireShared(arg);
}
// 采用自旋机制，在线程挂起之前不断尝试获取锁，不同于独占锁的是一旦获取共享锁之后，会调用 setHeadAndPropagate 方法同时唤醒后继节点，实现共享模式
private void doAcquireShared(int arg) {
  // 添加共享锁类型节点到队列中
  final Node node = addWaiter(Node.SHARED);
  boolean failed = true;
  try {
    boolean interrupted = false;
    for (;;) {
      final Node p = node.predecessor();
      if (p == head) {
        // 再次尝试获取共享锁
        int r = tryAcquireShared(arg);
        // 如果在这里成功获取共享锁，会进入共享锁唤醒逻辑
        if (r >= 0) {
          // 共享锁唤醒逻辑
          setHeadAndPropagate(node, r);
          p.next = null; // help GC
          if (interrupted)
            selfInterrupt();
          failed = false;
          return;
        }
      }
      // 与独占锁相同的挂起逻辑
      if (shouldParkAfterFailedAcquire(p, node) &&
          parkAndCheckInterrupt())
        interrupted = true;
    }
  } finally {
    if (failed)
      cancelAcquire(node);
  }
}

private void setHeadAndPropagate(Node node, int propagate) {
  // 头节点
  Node h = head; 
  // 设置当前节点为新的头节点
  // 这里不需要加锁操作，因为获取共享锁后，会从FIFO队列中依次唤醒队列，并不会产生并发安全问题
  setHead(node);
  if (propagate > 0 || h == null || h.waitStatus < 0 ||
      (h = head) == null || h.waitStatus < 0) {
    // 后继节点
    Node s = node.next;
    // 如果后继节点为空或者后继节点为共享类型，则进行唤醒后继节点
    // 这里后继节点为空意思是只剩下当前头节点了
    if (s == null || s.isShared())
      doReleaseShared();
  }
}
```

## CLH

## MCS

## Queue