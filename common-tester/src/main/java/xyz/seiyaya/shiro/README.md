# Shiro

## 组件
1. Authentication: 身份认证/登录，验证用户是不是拥有相应的身份
2. Authorization: 权限认证，验证某个已认证的用户是否有某个权限
3. Session Manager: 会话管理，即用户登录后就是一次会话，在没有退出之前，它的所有信息都在会话中
4. Cryptography: 加密，保证数据安全
5. Web Support: web支持
6. Concurrency: 支持多线程并发认证，一个线程中开启一个线程可以将权限传递过去
7. Testing: 测试支持
8. Run as: 允许一个用户假装另外一个用户去访问
9. Remember Me: 一次登录下次不需要再进行认证

shiro不会维护响应的权限、角色、用户之间的关系，这些需要我们注入在回调里面自己去声明


## 核心角色
1. Subject: 代表的是当前用户，所有的Subject都绑定到SecurityManager,与Subject的交互都是通过SecurityManager,Subject可以认为是一个门面
2. SecurityManager: 安全管理器，所有安全相关的操作都会与其交互，可以认为是SpringMVC中的DispatcherServlet
3. Realm: 域，Shiro从Realm中获取安全的数据(用户、角色、权限)，可以把这个当做数据源，提供给SecurityManager判断权限的数据来源

通过Subject来进行认证，而Subject的操作又委托给了SecurityManager,然后向其注入Realm根据subject信息判断是否符合操作权限

## 核心类
1. Subject: 用户，与应用交互的一方
2. SecurityManager: 具体操作Subject以及决定操作能否执行的管理器，进行认证和授权、会话和缓存管理
3. Authenticator: 认证器，负责Subject认证
4. Authrizer: 授权期，用来决定Subject能否有操作权限
5. Realm: 安全的数据源，实现可能是JDBC、localCache、redisCache等
6. SessionManager: 管理会话
7. SessionDAO: 会话存储的地方，可以是数据库、redis、localCache
8. CacheManager: 缓存控制器，用来管理如用户、角色、权限等的缓存
9. Cryptography: 密码模块

## 身份认证
+ shiro身份认证需要`principals`(身份)和`credentials`(证明)   (简单的说就是用户名和密码)
    - principals: 身份，即Subject的标识属性，用户名邮箱等，一个Subject可以有多个属性标识，但是只能有一个主的属性标识
    - credentials: 只有Subject唯一对应的密码值
+ xyz.seiyaya.shiro.LoginLogoutTest.testLogin
    - 首先调用`Subject.login(token)`方法登录，将其委托为SecurityManager,调用之前必须设置SecurityUtils.setSecurityManager(securityManager);
    - SecurityManager负责真正的身份逻辑校验，它又会给委托给Authenticator进行身份认证，它又可能委托给相应的AuthenticationStrategy进行多Realm认证
    - 默认情况下ModularRealmAuthenticator会调用AuthenticationStrategy进行多Realm身份认证
    - Authenticator会把相应的token传如Realm，从Realm获取认证信息
+ `Authenticator`与`AuthenticationStrategy`
    - Authenticator负责验证用户帐号，是shiro的验证帐号的核心入口点,如果验证成功返回`AuthenticationInfo`信息，该类封装了身份信息和证明信息，失败抛出异常
    - `SecurityManager`继承了Authenticator,其委托给多个Realm进行认证，校验的规则通过AuthenticationStrategy指定
    - FirstSuccessfulStrategy 只要有一个realm验证通过即通过验证(只返回第一个验证的信息)
    - AtLeastOneSuccessfulStrategy  同上，但是会返回所有的realm的验证信息
    - AllSuccessfulStrategy  所有realm认证通过才会返回
## 授权
在应用中规定哪些Subject可以访问哪些资源
### 角色

+ 主题(Subject): 访问的用户
+ 资源(Resources): 具体的操作功能(对应controller中的方法或者是一个图片文件等等)
+ 权限(permission): 决定用户是否有操作响应功能的权限
+ 角色(role): 权限的集合

### 授权方式
+ 授权方式
    1. 编程
    ```java
    Subject subject = SecurityUtils.getSubject();
    if(subject.hasRole("admin")){
        //有权限
    }
    ```
    2. 注解: @RequiresRoles("admin")
+ 授权
    1. 基于角色的访问控制(隐式角色   shiro-role.ini)
        - 只规定了角色，并没有指定角色可以进行什么操作
        - 使用hasRole()和hasAllRoles()等方法判断当前登录的subject是否有相应的角色
    2. 基于角色的访问控制(显示角色   shiro-permission.ini)
        - 指定了角色具有哪些权限
        - isPermitted()和isPermittedAll()判断是否当前登录的subject有相应的权限
+ 授权流程
    1. 首先调用 Subject.isPermitted*/hasRole*接口，将其委托为SecurityManager,最终委托给Authorizer
    2. Authorizer 是真正的授权者，调用isPermitted("user:view")，其首先会通过PermissionResolver把字符串转换成相应的Permission实例
    3. 在进行授权之前，其会调用相应的 Realm 获取 Subject 相应的角色/权限用于匹配传入的角色/权限
    4. Authorizer 会判断 Realm 的角色/权限是否和传入的匹配，如果有多个 Realm，会委托给 ModularRealmAuthorizer 进行循环判断，如果匹配如 isPermitted*/hasRole*会返回 true，
        否则返回 false 表示授权失败

+ Authorizer、PermissionResolver、RolePermissionResolver
    - Authorizer的职责是进行授权(访问控制)，是授权的核心入口，需要设置权限解析器`PermissionResolver`,也可以设置相应的角色解析器`RolePermissionResolver`
    - PermissionResolver 用于解析权限字符串成 Permission 实例
    - RolePermissionResolver 用于根据角色解析相应的权限集合
## ini文件配置
shiro提供通过代码配置securityManager和通过ini文件配置，有点类似Spring的DI/IOC的功能

## 编码/加密
Shiro默认提供了base64和16进制字符串编码/解码的API支持
### 散列算法
一般是不可逆的，适用于密码的存储，主要的散列算法有MD5、SHA

## Realm及其相关对象
