## mybatis测试
1. 整合spring后不加上事务localCache会失效，默认是每执行一次sql就提交事务，详见`SqlSessionTemplate`


## spring测试
1. 在一个service里面调用这个类里面的service，被调用的不是代理方法，所以两个方法会在同一个事务里面，不论你是否在被调用方法上设置的传播行为是新建一个事务  
导致的结果就是两个方法要么同时成功要么同时失败