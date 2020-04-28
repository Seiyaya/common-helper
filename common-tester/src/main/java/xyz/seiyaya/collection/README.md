# 集合

## HashMap
+ 多线程下的HashMap有什么问题
```
if ((p = tab[i = (n - 1) & hash]) == null)
     tab[i] = newNode(hash, key, value, null);
putVal方法中这个判断可能存在两个线程竞争的方法，会造成元素的覆盖，最终集合元素数量少于期望数量
```

底层数据结构就是数组+链表实现(jdk1.8重构成红黑树,链表长度大于8,tab长度大于64)  
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

## ConcurrentHashMap
+ 1.7的`锁分段`
```
本质是将原有的tab数组划分成多个segment。整体上看是有segment数组，每个segment数组又是一个HashEntry数组
类中的常量:
// 默认初始容量
static final int DEFAULT_INITIAL_CAPACITY = 16;
// 默认的负载因子，决定扩容时机
static final float DEFAULT_LOAD_FACTOR = 0.75f;
// 理论的并发数量，即是Segment数组的大小
static final int DEFAULT_CONCURRENCY_LEVEL = 16;
// 最大容量
static final int MAXIMUM_CAPACITY = 1 << 30;
// 每个Segment中table数组的最小长度为2，且必须是2的n次幂。
static final int MIN_SEGMENT_TABLE_CAPACITY = 2;
// 用于限制Segment数量的最大值，必须是2的n次幂
static final int MAX_SEGMENTS = 1 << 16;
// 在size方法和containsValue方法，会优先采用乐观的方式不加锁，直到重试次数达到2，才会对所有Segment加锁
// 这个值的设定，是为了避免无限次的重试。后边size方法会详讲怎么实现乐观机制的。
static final int RETRIES_BEFORE_LOCK = 2;
// segment掩码值，用于根据元素的hash值定位所在的 Segment 下标。
final int segmentMask;
// 和 segmentMask 配合使用来定位 Segment 的数组下标
final int segmentShift;
// Segment 组成的数组，每一个 Segment 都可以看做是一个特殊的 HashMap
final Segment<K,V>[] segments;
//Segment 对象，继承自 ReentrantLock 可重入锁，内部属性和方法和HashMap方法差不多
static final class Segment<K,V> extends ReentrantLock implements Serializable {
// 用于计算最大尝试次数
static final int MAX_SCAN_RETRIES = Runtime.getRuntime().availableProcessors() > 1 ? 64 : 1;
//用于表示每个Segment中的 table，是一个用HashEntry组成的数组
transient volatile HashEntry<K,V>[] table;
// Segment中元素的数量，每个Segment单独计数
transient int count;
// 修改时该变量会++
transient int modCount;
// 扩容阈值，和hashmap一样也是容器容量*负载因子
transient int threshold;
// 负载因子
final float loadFactor;
// segment中的元素类型，用于键值对的存储和维护单向链表
static final class HashEntry<K,V> {
```

+ 常用方法
```
构造函数: 主要是初始化内部的一些属性
put方法:
public V put(K key, V value){
    Segment<K,V> s;
    if ( value == null) throws NPE;
    int hash = hash(key);
    // 计算在segment数组的哪一个位置
    int j = (hash >>> segmentShift) & segmentMask;
    if ((s = (Segment<K,V>)UNSAFE.getObject(segments, (j << SSHIFT) + SBASE)) == null)
        s = ensureSegment(j);
    s.put(key, hash, value, false);
}
```
+ 1.8的`Synchronized + CAS`
```
void putVal(K k,V v,boolean onlyIfAbsent){
    int hash = spread(key.hashCode());
    //用来计算当前链表上的元素个数，树结构的话直接为2
    int binCount = 0;
    for (Node<K,V>[] tab = table;;) {
        // f表示tab数组i位置的元素，n表示数组长度，fh表示节点的hash值
        Node<K,V> f; int n, i, fh;
        // tab数组没有初始化
        if (tab == null || (n = tab.length) == 0)
            tab = initTable();
        // tab数组要插入的位置还没有元素
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) 
            if (casTabAt(tab, i, null, new Node<K,V>(hash, key, value, null)))
                break;
        // 当前tab数组正在扩容
        else if ((fh = f.hash) == MOVED)
            tab = helpTransfer(tab, f);
        // 要插入的位置有元素，需要判断是链表结构还是树结构，然后进行相应操作
        else
            // 校验tab数组的i位置的节点是否有变化
            if (tabAt(tab, i) == f) {
                if (fh >= 0) {
                    //链表结构的操作，将f节点添加到链表的尾部
                }else if(f instanceof TreeBin){
                    // 树结构的操作，调用f.putTreeVal()
                }
            }
            if (binCount >= TREEIFY_THRESHOLD)
                // 判断是否需要将链表转换成红黑树
                treeifyBin(tab, i);
    }
    // 元素个数+1(还可能触发扩容)
    addCount(1L, binCount);
}

void initTable(){
    // sizeCtl默认值为0，-1时表示其他线程在初始化表，初始化完成后会变成扩容后的阈值，小于-1时表示有几个线程在帮助扩容，上面的helpTransfer方法
    while ((tab = table) == null || tab.length == 0) {
        if ((sc = sizeCtl) < 0)
            // 其他线程正在执行初始化方法，等待即可
            Thread.yield();
        else if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {
            // 当前线程开始初始化tab
            sc = n - (n >>> 2); // n - 0.25n = 0.75n
        }
}

void addCount(long x, int check){
    // map的元素数量+x,由于可能多个线程都会+x，采用分治思想
}
```

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