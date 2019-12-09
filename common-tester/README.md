## mybatis测试
1. 整合spring后不加上事务localCache会失效，默认是每执行一次sql就提交事务，详见`SqlSessionTemplate`


## spring测试
1. 在一个service里面调用这个类里面的service，被调用的不是代理方法，所以两个方法会在同一个事务里面，不论你是否在被调用方法上设置的传播行为是新建一个事务  
导致的结果就是两个方法要么同时成功要么同时失败

## dubbo
### SPI(Service Provider Interface)
+ 服务发现机制,本质是将接口实现类的全限定名配置在文件中,并由服务加载器读取配置文件,加载实现类,Dubbo就是通过SPI机制加载所有的组件
    - java SPI: xyz.seiyaya.dubbo.spi.SpiTest.sayHello   需要在资源目录下建立接口的全路径名称的文件，里面填充实现类
    - dubbo SPI: