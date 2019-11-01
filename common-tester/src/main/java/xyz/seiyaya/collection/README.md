# 集合

## HashMap
底层数据结构就是数组+链表实现(jdk1.8重构成红黑树,链表长度大于8)  
1. 构造函数:含有三个关键属性`loadFactor` 、`threshold` 、`initialCapacity`  
loadFactor: 负载因子，主要的作用是决定是否扩容的一个参数，已存储元素数量/size > loadFactor则进行扩容,原因是尽量避免元素过多导致hash冲突  
threshold: 最大的可以存储的元素数量,数组的大小*loadFactor，数组的大小再初始化的时候会得到大于该值的最大的2次幂，是为了&运算计算位置时都以hashCode为准
2. get方法
先计算key的在数组中的位置，然后直接`tab[(n-1)&hash]`得到那个位置存储的元素,如果得到的元素key的hash和equals都相同就直接返回，最终返回结点的value

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

## ArrayList

## LinkedList

## LinkedHashMap

## TreeMap

## ConcurrentHashMap

## AQS

## CLH

## MCS

## Queue