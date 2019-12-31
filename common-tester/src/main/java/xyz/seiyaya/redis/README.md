# Redis设计与实现
## 数据结构与对象
Redis中每个键值对都是通过字符串`key`和对象`value`组成, 它可以是 字符串、 list 、 hash object 、 set object 、 sorted set object这五种对象中的一种