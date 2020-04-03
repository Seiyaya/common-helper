# spring 
## 生命周期(针对单实例的bean)
1. 实例化bean对象
+ 循环引用问题，不同的注入方式具体的结果也不同，构造器注入无法解决循环注入问题，setter注入则可以依次注入
2. 设置对象属性
3. 检查Aware相关接口并设置相关依赖
4. BeanPostProcessor前置处理
5. 检查是否实现了InitializingBean以决定是否调用afterPropertiesSet方法
6. 检查配置是否定义了init-method方法
7. BeanPostProcessor后置处理
8. 注册必要的接口,Destruction相关回调接口 
9. 业务功能---
10. 是否实现了DisposableBean接口
11. 是否配置了自定义destroy方法


## Spring中的重要注解
1. @Bean: 向容器中注册bean,注册的类型是返回值的类型，id默认为方法名作为id,可以通过name属性指定bean的id
+ 相关生命周期
    - 可以指定`initMethod`方法和`destroyMethod`
    - 实现接口`InitializingBean`,`DisposableBean`
    - 在bean中添加注解`@PostConstruct`，`@PreDestroy`
    - BeanPostProcessor后置处理器
2. @Value: 向bean种注入属性，其本质都是使用`SpelExpressionParser`来解析，类似于mybatis用来解析的OGNLCache



## 遇到的问题
+ 调用两个数据源的mapper方法，正常情况下能否在一个出现异常后回滚另外一个
    - 不可以，因为两个是不同的事务管理器，只能回滚同一个事务管理器下的事务(详情可以查看`org.springframework.transaction.support.TransactionTemplate#rollbackOnException`方法)
+ mybatis+spring为什么一级缓存会失效
    - 在同一个service里面调用两次mapper的同一个查询方法，还是会查询两次，没有使用到session的一级缓存，是因为没有加上事务的情况下，每一次都是重新创建一个sqlSession
    - 导致一级缓存失效，在添加事务处理后(加上@org.springframework.transaction.annotation.Transactional注解)，同一个方法内公用的同一个session
+ synchronized+事务注解导致的锁失效
    - 因为service方法是被代理过的，所以只能保证代理方法的执行的时候具备了加的锁，在AOP提交事务的时候锁已经被释放，另外一个线程已经可以操作该service方法(此时事务还没有提交)
    - 最终的结果也可能有线程安全问题
    - 始终要记得调用service方法执行的是走的是代理类的逻辑，也就是在service里面调用A方法和service.A()方法不是同一个