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

