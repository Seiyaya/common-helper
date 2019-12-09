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


