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

## AQS

## CLH

## MCS

## Queue