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