# Dubbo

## Dubbo中的组件角色
+ provider: 服务提供者， 启动时向注册中心注册自己提供的服务
+ consumer: 调用远程服务的服务消费方，启动时向注册中心订阅自己所需的服务
+ Registry:
+ Monitor: 
+ Container:

## Dubbo的启动方式
+ xml配置: @see xyz.seiyaya.dubbo.provider.MainProviderStarter
+ 注解配置: @see xyz.seiyaya.dubbo.provider.config.AnnotationProvider
+ API启动: @see xyz.seiyaya.dubbo.provider.DemoProvider

## 注册中心
