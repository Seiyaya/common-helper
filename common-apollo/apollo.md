# 注意事项
+ 修改属性不会进行重启，所以修改端口号不能够直接生效
+ AdminService 和 ConfigService 都是基于 `apollo-assembly` 项目启动( ApolloApplication )

## App
+ 为什么需要同步
    - 管理平台是所有环境共有，config 和 admin 服务是每个环境一个
    - App在 portal 表示的是需要 管理 的app, 而在 AdminService 和 ConfigService 中表示的是 存在的 app
+ AppCreationEvent
    - 通知所有存活环境同步新的app
    - 同步之后还需要`创建命名空间`、`创建默认集群`、'Cluster的默认命名空间`

## 配置发布后的实时推送
+ 大致步骤
    1. 发布配置到`portal`
    2. `portal`发布配置到`AdminService`
    3. `AdminService` 发送ReleaseMessage(异步) 到 `ConfigService`
    4. `ConfigService`通知客户端
### Portal 发布配置
+ 对应上面的1，2步
    1. 发布配置
    2. 调用发布配置API
    3. 插入Release 到 configDB
    4. 插入ReleaseHistory: 记录每次 Release 相关的操作日志
    5. 插入ReleaseMessage

### AdminService 发布配置
+ 主要是上述第三步，AdminService自身发布配置之后，通知所有的ConfigService
    - 通过数据库实现简单的消息队列，实现AdminService发布配置到每个ConfigService
+ 实现方式
    1. Admin Service 在配置发布后会往 ReleaseMessage 表插入一条消息记录。消息内容就是配置发布的 AppId+Cluster+Namespace ，参见 DatabaseMessageSender 
    2. Config Service 有一个线程会每秒扫描一次 ReleaseMessage 表，看看是否有新的消息记录，参见 ReleaseMessageScanner
    3. Config Service 如果发现有新的消息记录，那么就会通知到所有的消息监听器（ReleaseMessageListener），如 NotificationControllerV2 ，消息监听器的注册过程参见 ConfigServiceAutoConfiguration
    4. NotificationControllerV2 得到配置发布的 AppId+Cluster+Namespace 后，会通知对应的客户端
    5. 123步都在`apollo-biz`里面的message模块实现