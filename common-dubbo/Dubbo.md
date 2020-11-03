# Dubbo

## Dubbo中的组件角色
+ provider: 服务提供者， 启动时向注册中心注册自己提供的服务，并且订阅动态配置configurations
+ consumer: 调用远程服务的服务消费方，启动时向注册中心订阅自己所需的服务。订阅providers、configurations、routers
+ Registry: 注册中心，用于服务的注册与发现
+ Monitor: 
+ Container:

![dubbo](https://www.seiyaya.xyz/images/notes/dubbo/2020/10/dubbo01.png "dubbo") 

+ dubbo-demo
    - consumer: com.alibaba.dubbo.demo.consumer.Consumer
    - provider: com.alibaba.dubbo.demo.provider.Provider

+ 模块分包
    1. dubbo-common：提供工具类和通用模型
    2. dubbo-remoting: 远程通信模块，提供通用的客户端和服务端的通讯功能。
        - dubbo-remoting-zookeeper: 相当于 Zookeeper Client ，和 Zookeeper Server 通信
        - dubbo-remoting-api: 定义了 Dubbo Client 和 Dubbo Server 的接口。下面是它的实现
            - dubbo-remoting-grizzly： 基于 Grizzly 实现
            - dubbo-remoting-http： 基于 Jetty 或 Tomcat 实现
            - dubbo-remoting-mina： 基于 Mina 实现
            - dubbo-remoting-netty： 基于 Netty 3 实现
            - dubbo-remoting-netty4： 基于 Netty 4 实现
            - dubbo-remoting-p2p： P2P 服务器。注册中心 dubbo-registry-multicast 项目的使用该项目
    3. dubbo-rpc: 远程调用模块：抽象各种协议，以及动态代理，只包含一对一的调用，不关心集群的管理。
        - dubbo-rpc-api: 抽象各种协议以及动态代理，实现了一对一的调用
        - dubbo-rpc-default: `dubbo://`协议
    4. dubbo-cluster: 将多个服务提供方伪装为一个提供方,包括：负载均衡, 集群容错，路由，分组聚合等
        - 容错
            - com.alibaba.dubbo.rpc.cluster.Cluster 接口 + com.alibaba.dubbo.rpc.cluster.support 包
            - Cluster 将 Directory 中的多个 Invoker 伪装成一个 Invoker，对上层透明，伪装过程包含了容错逻辑，调用失败后，重试另一个
        - 目录
            - com.alibaba.dubbo.rpc.cluster.Directory 接口 + com.alibaba.dubbo.rpc.cluster.directory 包
            - Directory 代表了多个 Invoker ，可以把它看成 List ，但与 List 不同的是，它的值可能是动态变化的，比如注册中心推送变更
        - 路由
            - com.alibaba.dubbo.rpc.cluster.Router 接口 + com.alibaba.dubbo.rpc.cluster.router 包
            - 负责从多个 Invoker 中按路由规则选出子集，比如读写分离，应用隔离等
        - 配置
            - com.alibaba.dubbo.rpc.cluster.Configurator 接口 + com.alibaba.dubbo.rpc.cluster.configurator 包
        - 负载均衡
            - com.alibaba.dubbo.rpc.cluster.LoadBalance 接口 + com.alibaba.dubbo.rpc.cluster.loadbalance 包
            - LoadBalance 负责从多个 Invoker 中选出具体的一个用于本次调用，选的过程包含了负载均衡算法，调用失败后，需要重选
        - 合并结果
            - com.alibaba.dubbo.rpc.cluster.Merger 接口 + com.alibaba.dubbo.rpc.cluster.merger 包
            - 合并返回结果，用于分组聚合
    5. dubbo-register: 基于注册中心下发地址的集群方式，以及对各种注册中心的抽象
        - dubbo-registry-api: 抽象注册中心的注册与发现接口
    6. dubbo-monitor: 监控模块：统计服务调用次数，调用时间的，调用链跟踪的服务。
    7. dubbo-config: 配置模块,是 Dubbo 对外的 API，用户通过 Config 使用Dubbo，隐藏 Dubbo 所有细节
        - dubbo-config-api: 实现了 `API 配置` 和 `属性配置` 功能
        - dubbo-config-spring: 实现了 `XML` 配置 和 `注解配置` 功能
    8. dubbo-container: 是一个 `Standlone` 的容器，以简单的 Main 加载 Spring 启动，因为服务通常不需要 Tomcat/JBoss 等 Web 容器的特性，没必要用 Web 容器去加载服务。
        - dubbo-container-api: 定义了 com.alibaba.dubbo.container.Container 接口，并提供 加载所有容器启动的 Main 类
        - dubbo-container-spring: 提供了 com.alibaba.dubbo.container.spring.SpringContainer
        - dubbo-container-log4j: 提供了 com.alibaba.dubbo.container.log4j.Log4jContainer
        - dubbo-container-logback: 提供了 com.alibaba.dubbo.container.logback.LogbackContainer
    9. dubbo-filter: 过滤器模块：提供了内置的过滤器。
        - dubbo-filter-cache: 缓存过滤器
        - dubbo-filter-validation: 参数验证过滤器
    10. dubbo-plugin: 插件模块：提供了内置的插件
+ 通用模型 URL
    - 所有扩展点参数都包含 URL 参数，URL 作为上下文信息贯穿整个扩展点设计体系。
    - URL 采用标准格式: protocol://username:password@host:port/path?key=value&key=value

![dubbo](https://www.seiyaya.xyz/images/notes/dubbo/2020/10/dubbo02.png "dubbo") 

+ dubbo-cluster过程

![dubbo](https://www.seiyaya.xyz/images/notes/dubbo/2020/10/dubbo03.png "dubbo") 

## Dubbo的启动方式
+ xml配置: @see xyz.seiyaya.dubbo.provider.MainProviderStarter
+ 注解配置: @see xyz.seiyaya.dubbo.provider.config.AnnotationProvider
+ API启动: @see xyz.seiyaya.dubbo.provider.DemoProvider
+ 属性配置

+ 四个配置
    - application-shared
    - provider-side
    - consumer-side
    - sub-config
+ 配置读取
    - AbstractConfig#appendParameters(parameters, config, prefix): 将配置对象的属性添加到配置集合
    - AbstractConfig#appendAttributes(parameters, config, prefix): 将 @Parameter(attribute = true) 配置对象的属性，添加到参数集合。代码如下
    - AbstractConfig#appendProperties(config): 读取环境变量和 properties 配置到配置对象
    - AbstractConfig##appendAnnotation(annotationClass, annotation): 读取注解配置到配置对象

+ AbstractConfig的实现
    - ApplicationConfig: 应用配置
    - RegistryConfig: 注册中心配置
    - ModuleConfig: 模块信息配置
    - MonitorConfig: 监控中心配置
    - ArgumentConfig: 方法参数配置
    - ProtocolConfig: 服务提供者协议配置
    - AbstractMethodConfig: 方法级配置的抽象类
    - MethodConfig: 继承 AbstractMethodConfig ，方法级配置
    - AbstractInterfaceConfig: 继承 AbstractMethodConfig ，抽象接口配置类
    - AbstractServiceConfig: 实现 AbstractInterfaceConfig ，抽象服务配置类
    - ProviderConfig: 实现 AbstractServiceConfig ，服务提供者缺省值配置
    - ServiceConfig: 服务提供者暴露服务配置类
+ URL
+ @Parameter
    - Parameter 参数注解，用于 Dubbo URL 的 parameters 拼接,在配置对象的 getting 方法上
+ com.alibaba.dubbo.common.Version.getVersion(java.lang.Class<?>, java.lang.String)
    - 从 MAINFEST.MF 中获得版本号
    - 上述获取不到，从 jar 包命名中可能带的版本号作为结果
    - 返回版本号。若不存在，返回默认版本号
+ consumer相关的配置类
    - AbstractReferenceConfig： 实现 AbstractInterfaceConfig ，抽象引用配置类
    - ConsumerConfig： 实现 AbstractReferenceConfig ，服务消费者缺省值配置
    - ReferenceConfig： 服务消费者引用服务配置类

+ 属性配置(properties文件)
    - 如果公共配置很简单，没有多注册中心，多协议等情况，或者想多个 Spring 容器想共享配置，可以使用 dubbo.properties 作为缺省配置
    - Dubbo 将自动加载 classpath 根目录下的 dubbo.properties，可以通过JVM启动参数 -Ddubbo.properties.file=xxx.properties 改变缺省配置位置
    - com.alibaba.dubbo.config.AbstractConfig.appendProperties(config)
+ 注解配置
    - @EnableDubbo: 是 `@EnableDubboConfig`(开启 Dubbo Config) 和 `@DubboComponentScan`(扫描 Dubbo @Service 和 @Reference Bean) 的组合注解
+ @EnableDubboConfig的multiple属性
    - 创建 DubboConfigConfiguration.Multiple 或 DubboConfigConfiguration.Single 对象
+ @EnableDubboConfigBindings
    - 是 @EnableDubboConfigBinding 注解的数组
    
## 自动配置(autoconfigure)
来自模块 `dubbo-spring-boot-autoconfigure` 

1. DubboAutoConfiguration
2. RelaxedDubboConfigBinder：负责将 Spring Boot 的配置属性，注入到 Dubbo AbstractConfig 配置对象中
3. DubboDefaultPropertiesEnvironmentPostProcessor: 生成 Dubbo 默认的配置，添加到 `environment` 中 
    - 只要配置了 "spring.application.name" 的属性，"dubbo.application.name" 就会自动生成
4. WelcomeLogoApplicationListener: 处理 ApplicationEnvironmentPreparedEvent 事件，从而打印 Dubbo Banner 文本
5. OverrideDubboConfigApplicationListener: 处理 ApplicationEnvironmentPreparedEvent 事件，根据 "dubbo.config.override" 的属性值，若为 true 时，则覆盖 environment 中 "dubbo." 开头的配置
6. AbstractDubboEndpoint: 提供给子类工具方法
7. DubboEndpoint: 获得 Dubbo Meta Data（元数据）
8. DubboConfigsMetadataEndpoint: 获得 所有的 Dubbo 配置类的元数据


## 核心流程

![dubbo](https://www.seiyaya.xyz/images/notes/dubbo/2020/10/dubbo04.png "dubbo") 

### 各层说明
+ Service业务层: 业务代码的接口与实现
+ Config配置层： 对外配置接口，以 ServiceConfig, ReferenceConfig 为中心，可以直接初始化配置类，也可以通过 Spring 解析配置生成配置类
+ proxy服务代理层: 服务接口透明代理，生成服务的客户端 Stub 和服务器端 Skeleton
+ registry注册中心层：封装服务地址的注册与发现，以服务 URL 为中心,扩展接口为 RegistryFactory, Registry, RegistryService 
+ cluster路由层: 封装多个提供者的路由及负载均衡，并桥接注册中心，以 Invoker 为中心，扩展接口为 Cluster, Directory, Router, LoadBalance
+ monitor监控层: RPC 调用次数和调用时间监控，以 Statistics 为中心，扩展接口为 MonitorFactory, Monitor, MonitorService
+ protocol远程调用层: 封将 RPC 调用，以 Invocation, Result 为中心，扩展接口为 Protocol, Invoker, Exporter
+ exchange信息交换层: 封装请求响应模式，同步转异步，以 Request, Response 为中心，扩展接口为 Exchanger, ExchangeChannel, ExchangeClient, ExchangeServer
+ transport 网络传输层: 抽象 mina 和 netty 为统一接口，以 Message 为中心，扩展接口为 Channel, Transporter, Client, Server, Codec
+ serialize 数据序列化层: 可复用的一些工具，扩展接口为 Serialization, ObjectInput, ObjectOutput, ThreadPool 

### 调用关系

![dubbo](https://www.seiyaya.xyz/images/notes/dubbo/2020/10/dubbo05.png "dubbo") 


### 领域模型
+ Invoker
    - 可以是本地调用，也可以是远程调用，也可能是集群调用
    - 消费者端是Invoker(DubboInvoker、HessianRpcInvoker)调用提供者端的Exporter
    - 提供者端是生成Invoker(AbstractInvoker)会顺带生成一个Exporter,Exporter负责与消费者端的Invoker交互
+ Invocation
    - Invocation 是会话域，它持有调用过程中的变量，比如方法名，参数
+ Result
    - Result 是会话域，它持有调用过程中返回值，异常等
+ Filter: 过滤器接口
+ ProxyFactory: 代理工厂接口
    - 服务消费者调用: ReferenceConfig --> Protocol --> Invoker --> ProxyFactory --> ref
    - 服务提供者调用: ServiceConfig   --> ProxyFactory --> Invoker --> Protocol --> Exporter
    - Dubbo 支持 Javassist 和 JDK Proxy 两种方式生成代理
+ Protocol: Protocol 是服务域，它是 Invoker 暴露和引用的主功能入口。它负责 Invoker 的生命周期管理
+ Exporter: Invoker 暴露服务在 Protocol 上的对象
+ InvokerListener
+ ExporterListener

## 注册中心
+ 在 dubbo-registry 模块中可以看到支持的注册中心： ZooKeeper、Redis、Simple、Multicast
+ 也可以基于 RegistryFactory 和 Registry进行扩展
### 注册中心工作流程
1. service provider启动时，向注册中心写入自己的元数据信息，同时会订阅配置元数据信息
2. service consumer启动时，向注册中心写入自己的元数据信息，并订阅服务提供者、路由和配置元数据信息
3. dubbo-admin(服务治理中心)启动时，会订阅所有的消费者、服务提供者、路由和配置元数据信息
4. 当service provider或者service consumer加入或者离开，注册中心服务提供者目录会发生变化，变化信息会动态通知给消费者、服务治理中心
5. 当service consumer发起服务调用的时候，会异步将调用、统计信息等上报给监控中心(dubbo-monitor-simple)
### 注册中心数据结构
+ dubbo使用zookeeper只有临时节点和持久节点，不需要具有顺序的节点
```
/dubbo  在registry中配置的group属性，默认为dubbo
    /service
        /providers  包含的接口有多个服务者URL元数据信息
        /consumers  包含的接口有多个消费者URL元数据信息
        /routers    包含多个用于消费者路由策略的元数据信息
        /configurators  用于服务动态配置URL元数据信息
```
+ 基于redis的注册中心
    - 使用hash的数据结构   key: /dubbo/package/providers  hkey: url hvalue:存储的值
+ 客户端实现
    - zkClient
    - curator   默认的实现
    - zookeeper采用的是 事件通知 + 客户端拉取，客户端在zookeeper上注册watcher，服务端相关数据变更通知客户端(会对configurations进行全量拉取)
### 订阅发布的实现
+ zookeeper作为注册中心可以查看 ZooKeeperRegistry#doSubscribe
+ redis作为注册中心查看 RedisRegistry#doSubscribe
### 缓存机制
+ dubbo中实现了通用的缓存机制，用来存储zk的配置信息，详见： AbstractRegistry
+ 缓存的加载 AbstractRegistry#loadProperties
+ 缓存的保存有同步和异步， 异步采用线程池，如果失败则不断重试, 详见 AbstractRegistry#saveProperties
```
//用来存储服务缓存对象的结构
notified = new ConcurrentHashMap<URL, Map<String, List<URL>>>();
外部的key  消费者的url
内层map
    key 分类，providers、consumers、routes、configurators
    value 对应的服务列表
```
### 重试机制
+ ZooKeeperRegistry 和 RedisRegistry 继承了 FailbackRegistry，具备了失败重试的机制
+ FailbackRegistry 中定义了一个ScheduleExecutorService ，每经过固定时间(默认为5s)调用retry()方法，
    2.7.6之后重新做了修改，将定时的任务抽象了一个 HashedWheelTimer 
### 设计模式
+ 模板模式: 注册中心的逻辑都是用了模板逻辑，每层抽象类都实现了通用功能
+ 工厂模式: 所有注册中心的实现都是通过工厂产生的  RegistryFactory，可以是zk的也可以是redis的

## Dubbo扩展点加载机制
所在模块: `dubbo-common` 的 `extension`包下面

### ExtensionLoader属性
+ SERVICES_DIRECTORY 和 DUBBO_DIRECTORY
    - 这两个目录下放置 `接口全限定名` 配置文件，每行内容为 `扩展名=扩展实现类全限定名`
+ NAME_SEPARATOR
    - 扩展名分隔符，使用逗号
+ 静态属性
    - 一个拓展( 拓展接口 )对应一个 ExtensionLoader 对象
+ 对象属性
    - 一个拓展通过其 `ExtensionLoader` 对象，加载它的拓展实现们
    - 缓存加载的拓展配置
    - 缓存创建的拓展实现对象
+ 扩展点自动包装
+ 扩展点自动装配
+ 扩展点自适应
+ 扩展点自动激活
### 加载机制
+ java SPI存在的问题
    - 会一次初始化所有的扩展点，没有使用的扩展也会被加载
    - 如果扩展加载失败，扩展的名称也获取不到,可能也会到只异常信息消失
+ dubbo SPI的改进
    - 增加了扩展IOC和AOP的支持，一个扩展可以直接setter注入其他扩展
    - dubbo spi可以分为class缓存、实例缓存，这两种又能根据扩展类的种类分为普通、包装(Wrapper类)、自适应(Adaptive)
+ 扩展点的缓存
    - class缓存: 缓存中不存在则加载配置文件，根据配置将class缓存到内存中，不会全部初始化
    - 实例缓存: 按需缓存
```
缓存信息的位置，详见 @see ExtensionLoader
cachedClasses 普通扩展类缓存，不包括自适应扩展类和Wrapper类
cachedWrapperClasses Wrapper类缓存
cachedAdaptiveClass  自适应扩展类缓存
cachedInstances      扩展名与扩展对象缓存
cachedAdaptiveInstance  实例化后的自适应扩展对象，只能同时存在一个
cachedNames          扩展类与扩展名缓存
EXTENSION_LOADERS    扩展类与对应的扩展类加载器缓存
EXTENSIONJNSTANCES   扩展类与类初始化后的实例
cachedActivates      扩展名与@Activate的缓存
```
+ 扩展点的分类
    - 普通扩展类: 在SPI配置文件中的扩展实现
    - 包装扩展类: 没有具体的实现，只是做了通用逻辑的抽象，并且需要构造方法传入一个具体的扩展接口的实现
    - 自适应扩展类: 一个扩展接口多个实现，具体使用哪个类通过url的参数控制，使用@Adaptive来实现
+ 扩展点的特性
    - 自动包装: ExtensionLoader 在加载扩展的时候，如果这个扩展类需要其他扩展点作为构造函数的参数，则这个扩展类被认为Wrapper类 @see: ProtocolFilterWrapper
    - 自动装载: 使用setter方法进行属性设置，如果该注入的属性有多个实现，通过下面的自适应解决
    - 自适应: 通过 @Adaptive 注解来确定具体使用哪个类
    - 自动激活： @Activate 多个实现的时候默认被激活使用
### 扩展点注解
+ @SPI: 标记这个接口是SPI接口，可以有多个不同内置或用户自定义实现, getExtension()来获取扩展接口的具体实现，会对class和是否有spi注解校验
+ @Adaptive: 只有 AdaptiveExtensionFactory 和 AdaptiveCompiler标注在类上，标注在接口的方法上会为接口生成代理类和一些通用逻辑
+ @Activate: 

### ExtensionLoader的工作原理
+ 是整个扩展机制的主要逻辑类，在这个类里面实现了配置的加载、扩展类缓存、自适应对象生成等所有工作
+ 逻辑入口
    - getExtension
    - getAdaptiveExtension
    - getActivateExtension
```
getExtension  核心加载方法，每一步都会检查缓存是否存在
1. 调用createExtension创建扩展类对象
2. 加载扩展类配置信息
3. 实例化扩展类
4. 查找匹配的包装类，并注入扩展类实例

getAdaptiveExtension
1. 初始化所有扩展类实现的集合
2. 根据URL条件激活所有符合条件的@Activate并排序
3. 根据用户URL配置的顺序，调整扩展类的顺序

getActivateExtension  只是根据不同条件同时激活多个普通扩展类
1. 加载扩展类配置信息
2. 调用createAdaptiveExtension创建自适应类
3. 生成自适应代码
4. 获取类加载器、编译器、编译自适应类
```
+ getExtension
    - 检查缓存中是否有现成的数据，没有则调用 createExtension 开始创建，这里有个特殊点，getExtension(name) name = true返回默认的扩展类
    - 调用createExtension创建的过程中，也会检查缓存中的配置信息，如果不存在扩展类，则从对应的目录文件中读取
    - 入口: @see:org.apache.dubbo.common.extension.ExtensionLoader#getExtension
+ getAdaptiveExtension: 为扩展点自动生成实现类字符串
    - 为接口中每个有 @Adaptive 注解的方式生成默认实现，没有注解的方法生成空实现
+ getActivateExtension: 获取所有自动激活扩展点
    - 检查缓存，如果缓存中没有，则初始化所有扩展类的实现
    - 遍历整个@Activate注解集合，然后根据@Activate中配置的before 、 after 、 order等参数进行排序
    - 遍历所有用户自定义扩展类名称，根据用户URL配置的顺序，调整扩展点激活顺序
    - 返回所有自动激活类集合
+ ExtensionFactory的实现原理
    - ExtensionLoader是作为SPI的核心，被 ExtensionFactory 创建
+ getExtensionClasses: 获得扩展类实现数组
    - cachedClasses: 缓存的拓展实现类集合,主要包含自适应扩展实现类、带唯一参数为扩展接口的 
    - cachedAdaptiveClass: 拓展 Adaptive 实现类，会添加到 cachedAdaptiveClass 属性中
    - cachedWrapperClasses: 拓展 Wrapper 实现类，会添加到 cachedWrapperClasses 属性中
+ loadExtensionClasses: 从多个配置文件中，加载扩展类实现数组
+ loadFile: 从一个配置文件中加载实现类数组
+ getExtensionLoader: 根据扩展点的接口获得扩展加载器
    - 必须是接口且被@SPI注解标记
+ getExtension： 获取指定扩展对象
+ injectExtension: 注入获取的扩展对象的属性(需要与set方法)
+ getAdaptiveExtension： 获取自适应扩展对象
+ createAdaptiveExtensionClass: 生成自适应扩展类
+ getExtensionLoader(url, key, group)： 获得符合自动激活条件的拓展对象数组

## 线程模型
+ 线程池实现: dubbo-common下面的threadpool包
    - fixed
    - cached
    - limited: 空闲时间无限大，导致不会回收暂时空闲的线程
+ 线程池的拒绝策略: com.alibaba.dubbo.common.threadpool.support.AbortPolicyWithReport.AbortPolicyWithReport
    - 打印警告日志
    - 输出到jstack
## 服务暴露
### 本地暴露(injvm)
<dubbo:service scope="local"/>
不配置scope默认两种方式都暴露
### 远程暴露
<dubbo:service scope="remote"/>
### 本地引用

### 远程引用

## Dubbo启动和停止
+ duboo提供3种配置方式：xml配置、注解、属性文件
+ BeanDefinitionParserDelegate#parseCustomElementCElementj BeanDefinition)
### 配置解析
+ 基于xml的配置解析: @see DubboNamespaceHandler
    - 主要把不同的标签关联到解析实现类中 registerBeanDefinitionParser 方法约定了在dubbo框架遇到标签application、module和registry等都会委托给 DubboBeanDefinitionParser 处理

+ 基于注解配置原理
+ 涉及到的注解
    - EnableDubbo： 激活注解
        - ServiceAnnotationBeanPostProcessor: 提升@Service注解的服务为Spring Bean
        - ReferenceAnnotationBeanPostProcessor: 注入@Reference引用
        - DubboConfigConfigurationSelector: 支持配置文件读取配置
    - 如果注解上有@Import，则会触发对应的 selectImports,比如 EnableDubboConfig 注解中指定的 DubboConfigConfigurationSelector#selectImports
### 服务暴露的实现原理
+ 配置承载初始化
    - -D添加JVM启动参数
    - 代码或者xml配置
    - dubbo.properties文件配置
+ 远程服务的暴露机制
    - 服务转换成Invoker
        - ServiceConfig: ref
        - ProxyFactory: Javassist、JDK动态代理
        - Invoker: AbstractProxyInvoker
    - Invoker转成Exporter
        - Protocol: dubbo、injvm
        - Exporter
    - 框架服务暴露的入口点: ServiceConfig#doExport
    - 如果配置了多个注册中心，则会再会在 ServiceConfig#doExportUrls 中依次暴露
### 服务消费的实现原理
### 优雅停机原理

## Dubbo远程调用
### dubbo调用
### dubbo协议详解
### 编解码器调用原理
### Telnet调用原理
### ChannelHandler

## Dubbo集群容错
### cluster层
### 容错机制的实现
### Directory的实现
### 路由的实现
### 负载均衡的实现
### Merger的实现
### Mock

## @Reference注解
### ReferenceAnnotationBeanPostProcessor
+ 针对`@Reference`注解进行后置处理
```java
/*
spring调用 postProcessPropertyValues 方法
    从 injectionMetadataCache 中查找 InjectionMetadata 数据
        从 buildReferenceMetadata 构建 添加了@Reference方法和属性的缓存
            最后注入对象 metadata.inject()
*/
// 缓存加了@Referece注解的元数据信息，key是bean的名称或者类名，value是加了@Reference的属性和方法
ConcurrentMap<String, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap<String, InjectionMetadata>(256);

// 缓存new过的ReferenceBean，相同的key，只会产生一个ReferenceBean 
// 可以通过获取该 ReferenceBean 最后得到代理的dubbo接口
ConcurrentMap<String, ReferenceBean<?>> referenceBeansCache = new ConcurrentHashMap<String, ReferenceBean<?>>();

// spring设置属性到bean之前调用该方法
@Override
public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeanCreationException {
    // 根据bean的类型，获取需要注入的元数据信息
    InjectionMetadata metadata = findReferenceMetadata(beanName, bean.getClass(), pvs);
    try {
        // 注入对象
        metadata.inject(bean, beanName, pvs);
    } catch (BeanCreationException ex) {
        throw ex;
    } catch (Throwable ex) {
        throw new BeanCreationException(beanName, "Injection of @Reference dependencies failed", ex);
    }
    return pvs;
}

// 获取注解元数据
private InjectionMetadata findReferenceMetadata(String beanName, Class<?> clazz, PropertyValues pvs) {
    // 如果是自定义的消费者，没有beanName，退化成使用类名作为缓存key
    String cacheKey = (StringUtils.hasLength(beanName) ? beanName : clazz.getName());
    // 双重检查，判断是否需要刷新注入信息
    ReferenceInjectionMetadata metadata = this.injectionMetadataCache.get(cacheKey);
    // 判断是否需要刷新
    if (InjectionMetadata.needsRefresh(metadata, clazz)) {
        // 第一次判断为需要刷新，则锁住injectionMetadataCache对象
        synchronized (this.injectionMetadataCache) {
            // 再次判断是否需要刷新
            metadata = this.injectionMetadataCache.get(cacheKey);
            if (InjectionMetadata.needsRefresh(metadata, clazz)) {
                // 需要刷新，而且原来缓存的信息不为空，清除缓存信息
                if (metadata != null) {
                    metadata.clear(pvs);
                }
                try {
                    // 生成新的元数据信息
                    metadata = buildReferenceMetadata(clazz);
                    // 放入缓存
                    this.injectionMetadataCache.put(cacheKey, metadata);
                } catch (NoClassDefFoundError err) {
                    throw new IllegalStateException("Failed to introspect bean class [" + clazz.getName() +
                            "] for reference metadata: could not find class that it depends on", err);
                }
            }
        }
    }
    return metadata;
}

private ReferenceInjectionMetadata buildReferenceMetadata(final Class<?> beanClass) {
    // 查找加了@Reference注解的属性
    Collection<ReferenceFieldElement> fieldElements = findFieldReferenceMetadata(beanClass);
    // 查找加了@Reference注解的方法
    Collection<ReferenceMethodElement> methodElements = findMethodReferenceMetadata(beanClass);
    return new ReferenceInjectionMetadata(beanClass, fieldElements, methodElements);
}

// 查找加了@Reference注解的属性
private List<ReferenceFieldElement> findFieldReferenceMetadata(final Class<?> beanClass) {
    // 保存加了@Reference注解的属性列表
    final List<ReferenceFieldElement> elements = new LinkedList<ReferenceFieldElement>();

    ReflectionUtils.doWithFields(beanClass, new ReflectionUtils.FieldCallback() {
        @Override
        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
            // 获取属性上的@Reference注解
            Reference reference = getAnnotation(field, Reference.class);
            // 如果存在@Reference注解
            if (reference != null) {
                // 不支持静态属性的注入
                if (Modifier.isStatic(field.getModifiers())) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("@Reference annotation is not supported on static fields: " + field);
                    }
                    return;
                }
                // 添加到队列里
                elements.add(new ReferenceFieldElement(field, reference));
            }

        }
    });
    return elements;
}
```