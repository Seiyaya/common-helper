# Dubbo

## Dubbo中的组件角色
+ provider: 服务提供者， 启动时向注册中心注册自己提供的服务，并且订阅动态配置configurations
+ consumer: 调用远程服务的服务消费方，启动时向注册中心订阅自己所需的服务。订阅providers、configurations、routers
+ Registry:
+ Monitor: 
+ Container:

## Dubbo的启动方式
+ xml配置: @see xyz.seiyaya.dubbo.provider.MainProviderStarter
+ 注解配置: @see xyz.seiyaya.dubbo.provider.config.AnnotationProvider
+ API启动: @see xyz.seiyaya.dubbo.provider.DemoProvider

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

### 扩展点动态编译的实现原理

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
/**
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