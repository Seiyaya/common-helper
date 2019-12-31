# Rabbit MQ

## 主要的角色
1. Producer: 投递消息的一方，创建消息发布到MQ，消息主要分为消息体(`payload`)和标签(`label`)
    - 标签主要是存储`路由键`和`交换机`
    - 消息体主要是序列化之后的内容，可能格式是字符串或者二进制的信息
2. Customer: 接收消息的一方，并订阅到队列上，消费消息是指的消费消息体，在消费路由的过程中标签会被丢弃，存入队列的只有消息体
    - 所以消息者不知道谁生产的消息，也不需要知道
3. Broker: 消息中间件的服务节点
    - 生产者将消息包装后，发送到Broker(AMQP协议对应了basic.publish)
    - 消费者订阅并接收消息，将消息反序列化然后处理消息(AMQP洗衣对应了basic.consume和basic.get)
4. Queue: 是rabbitMQ的内部对象，用于存储消息(kafka将消息存储在topic上)
    - 多个消费者订阅同一个生产者的消息，只有一个消费者可以消费这个消息，也就是说默认队列不支持广播
5. Exchange: 生产者的消息会被交换机发送到能够匹配上的队列，路由不到则返回给生产者或者直接丢弃
6. RoutingKey: 路由键，生产者将消息发送给消费者都会附带一个RoutingKey,通过它判断消息会被派发到哪个具体的队列上
7. Binding: 生产者将消息发送到交换机，由交换机决定消息被派发到绑定到该交换机的队列上
    - 在绑定消息的时候使用的是BindingKey
    - 在发送消息的时候使用的是RoutingKey
    - 在direct类型的交换机中是直接两个相等，但是topic类型的交换机可以不相等
8. Connection: 无论是生产者还是消费者都需要向Broker建立TCP连接(即为Connection)
9. Channel: 创立Connection后接着可以创建信道，每个信道都会被指派一个唯一的id,信道是建立在Connection之上的连接，RabbitMQ处理每条AMQP指令都是通过信道完成的

### Exchange
1. fanout: 它会将所有的发送到该交换机的消息路由到所有与该交换机绑定的交换机队列上
2. direct: 它会将发送的消息派发到BindingKey和RoutingKey相同的
3. topic: 拥有通配符匹配的交换机

## 运行流程
### 生产者
1. 生产者链接到RabbitMQ Broker上，建立一个连接，开启一个信道(Channel)
2. 生产者声明一个交换机，并设置相关属性，比如交换机类型、是否持久化、是否自动删除等
3. 生产者声明一个队列，并设置相关属性，是否排他、持久化、自动删除等
4. 生产者通过路由键将交换机和队列绑定到一起
5. 生产者发送消息到RabbitMMQ Broker上，其中包含路由键、交换机等信息
6. 交换机根据收到的信息查找对应的队列
    - 如果找到则将生产者的消息存入到对应的队列中
    - 如果没有找到则根据生产者配置的属性决定丢弃还是退还给生产者
7. 关闭信道
8. 关闭连接

### 消费者
1. 消费者链接到RabbitMQ Broker上，建立一个连接，开启一个信道(Channel)
2. 消费者向RabbitMQ Broker请求消费对应队列的消息，可能设置相应的回调函数，以及一些准备工作
3. 等待RabbitMQ Broker回应并投递相应队列中的消息，消费者接受消息
4. 消费者确认(ack)接收到消息
5. RabbitMQ从队列删除相应已经被确认的消息
6. 关闭信道
7. 关闭连接

### AMQP协议
RabbitMQ是遵循AMQP协议的，也就是说RabbitMQ是AMQP协议的Erlang实现
+ AMQP协议的层级
    - Module Layer: 位于协议的最高层，主要定义了一些供给客户端操作的命令，客户端可以用这些命令实现自己的业务逻辑
    - Session Layer: 位于中间层，主要负责将客户端的命令发送到服务器，再将服务端的应答返回给客户端，主要是为客户端和服务端之间提供可靠性的同步机制和错误处理
    - Transport Layer: 位于最底层，主要传输二进制数据流，提供帧的处理、信道复用、错误检测和数据展示

+ AMQP生产者具体的调用过程
```java
/**
这个方法会进一步封装成Protocol Header 0-9-1的报文发送给Broker,通知Broker采用对应的协议
然后Broker回返回Connection.Start来建立连接，在连接过程中涉及
Connection.Start    Start-OK   TUNE    TUNE-OK  OPEN    OPEN-OK
 */
Connection connection = connectionFactory.newConnection();
/**
客户端创建信道，其包装Channel.Open方法发送给Broker,等待Channel.Open-Ok命令
 */
Channel channel = connection.createChannel();
/**
客户端发送消息，调用对应的命令Basic.Publish,这个命令包含了Content Header和Content body 一个存储路由键信息，一个存储具体发送的数据
*/
channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
```

+ AMQP消费者具体的调用过程
```java
/**
建立连接和生产者一致，同样也设计到六个命令
如果channel.basicQos(64);方法设置最大未确认的消息，那么会涉及channel.basicQos和channel.Qos-Ok命令
*/
Connection connection = connectionFactory.newConnection(new Address[]{new Address(HOST_NAME,PORT)});
channel.basicQos(64);
/**
消费者消费消息之前，消费者客户端需要向Broker发送Basic.Consume命令，将Channel设置为接收模式，之后Broker返回命令Basic.Consumer-OK
紧接着Broker通过Basic.Deliver命令向消费者推送消息，同样也会携带header和body
消费者接收到消息并正确消费之后，向Broker发送Basic.Ack确认消息
在消费者停止消费的时候，主动关闭连接，和生产者那里一样
*/
channel.basicConsume(QUEUE_NAME,consumer);
```

## 客户端开发
### 连接RabbitMQ
Connection可以创建多个Channel实例，但是Channel在多个线程之间不共享，应用程序应该为每一个线程开辟一个Channel,多线程共享Channel是线程不安全的  
Channel和Connection都可以调用isOpen方法来判断通道或者连接是否开启,但是不推荐使用该方法，因为它依赖`shutdownCause`属性，该属性获取的时候添加了synchronized代码块  
如果判断isOpen方法会导致在一个应用上处理消息是串行化的进行

### 使用交换机和队列
使用之前需要先声明对应的组件，声明已有的组件需要方法的参数全部想用才不会抛出异常
+ exchangeDeclare参数列表
    - exchange: 交换机的名称
    - type: 交换机的类型，如fanout、direct、topic
    - durable: 是否进行持久化，持久化可以将交换机存盘，服务器重启的时候不会丢失相关消息
    - autoDelete: 设置是否自动删除，自动删除的条件是: 没有队列绑定到该交换机上(即以绑定队列后都进行了解绑操作)
    - internal: 设置是否是内置的，true表示是内置的交换机，客户端的无法直接将消息发送到对应的交换机，只能通过交换机路由
    - argument: 其他一下结构化参数，如alternate-exchange
+ queueDeclare参数列表
    - queue: 队列的名称
    - durable: 设置持久化: 为true则设置队列为持久化，持久化队列会存盘，重启服务器不会丢失消息
    - exclusive: 是否排他，该队列仅对首次声明它的连接可见，并在联机断开时自动删除，并在连接断开时自动删除，该队列不会被持久化
    - autoDelete: 是否设置自动删除,至少有一个消费者连接到这个队列，然后这个消费者断开连接，队列被删除
    - argument: x-message-ttl、x-expires、x-max-length、x-max-length-bytes、x-dead-letter-exchange
+ queueBind参数列表
    - queue: 队列名称
    - exchange: 交换机的名称
    - routingKey: 用来绑定交换机和队列的路由键
    - argument: 定义绑定的一些参数
    - unBind方法就可以进行解绑操作
+ exchangeBind参数列表: 交换机与交换机进行绑定
+ 发送消息
+ 消费消息: 可以通用push模式(Basic.Consume)和pull模式(basic.get)
+ 消费端的确认和拒绝
    - 消息确认机制: messageAcknowledgement,消费者在订阅队列时，可以指定autoAck参数，false表示需要等待消费者显示的回复确认消息后才从内容中移除消息
    - 拒绝消息: basicReject(deliveryTag,requeue);指定消息的编号以及是否将消息重新入队列，只能一次拒绝一条消息，批量拒绝消息需要使用basicNack方法，requeue设置为false可以启用死信队列的功能  
    死信队列可以通过检测被拒绝或者未送达的消息来追踪问题
+ 关闭连接: Connection和Channel采用同样的方式来管理网络失败、内部错误和显示关闭连接，他们的生命周期
    - Open: 开启状态，代表当前对象可以使用
    - Closing: 正在关闭状态，当前对象被显示的调用通知关闭方法，这样就产生一个关闭请求让内部对象进行相应的操作，并等待这些操作的完成
    - Closed： 已经关闭状态，当前对象已经接收到所有的内部对象已完成关闭动作的通知，并且其也关闭了自身
  Connection和Channel最终都会成为Closed状态，不论是程序调用关闭方法还是客户端异常或者网络异常，可以添加关闭状态转换的监听器

## RabbitMQ的特色
### 重要的参数
+ mandatory(强制性): 参数为true时，交换机无法根据自身的类型和路由键找到对应的队列的时候，那么RabbitMQ会调用`Basic.Retun`返回给生产者，为false直接将消息丢弃
    - 生产者回去被退回的消息: channel.addReturnListener();
+ immediate(即时): 参数为true时，如果交换机找到了对应的队列，但是队列上没有绑定对应的消费者，消息也会被退回(3.0后该功能被移除)
+ 备份交换器(alternate exchange): 不想使用mandatory的特性(需要编写监听器)，但是又想处理没有找到对应队列的消息，原理没有被路由的消息将会发到备用交换机，然后再发到备用的队列上
+ TTL: 
    - 设置消息的TTL：可以对队列和消息分别进行设置过期时间，两者都设置取较小的值，超过了TTL值的消息会变成死信，消费者将无法接受到消息(声明过期时间的单位是毫秒)
    - 设置队列的TTL：RabbitMQ只会保证最终过期的队列被删除，但是不能保证即时的删除，声明队列的时候指定(x-expires)
+ 死信队列: dead letter exchange(DLX)  一个消息不能被消费，将会被发送到这个交换机中，变成死信的条件
    - 拒绝消息 Basic.reject和Basic.Nack，设置参数requeue为false
    - 消息过期
    - 队列达到最大的长度
    - x-dead-letter-exchange  指定死信队列的交换机，x-dead-letter-routing-key指定死信的路由键
+ 延迟队列: 不想让消费者立即拿到消息，而是等待特定时间再进行消费
    - 场景: 订单系统的30分钟未支付的异常处理，家居服务的定时功能
    - 可以利用死信队列和TTL的机制来模拟，消费者订阅死信队列，消息设置过期时间后被发送到死信队列，从而到达延迟消费的功能
+ 优先队列: x-max-priority参数来指定，优先级高的消息将优先被消费，WEB管理页面的标识是`Pri`，优先级范围是[0,10],默认为5
+ 持久化(durable)
    - 交换机持久化
    - 队列持久化
    - 消息持久化: 一般来说消息持久化和队列持久化会搭配使用
    - 持久化后仍然存在的问题: 消费者接受到消息但是出现异常没有处理消息(ack机制解决)，持久化消息正确存入RabbitMQ后，还没有写入到磁盘的时候宕机(不是同步写入磁盘)，使用集群解决
    - 消息持久化是用来解决，服务器异常或者崩溃而导致的消息丢失

### 生产者确认
+ 即生产者如何知道消息被成功的发往了RabbitMQ
    - 事务机制
    - 发送确认机制(publisher confirm)
+ 事务机制: 与事务机制有关的方法有：channel.txSelect(设置事务模式)、channel.txCommit、channel.txRollback
    - 性能方面不是特别好
+ 发送方确认机制
    - 生产者将channel设置为发送确认模式，所有在该信道上发布的消息都会被指派一个唯一的id(从1开始)，RabbitMQ收到消息就会Basic.ack给生产者(包含之前产生的id)
    - 持久化模式下，RabbitMQ会持久化后再进行消息的确认
    - 事务机制在生产者发送一条消息后会阻塞等待RabbitMQ的确认，而发送确认模式是异步的，只需要等待MQ回传编号即可


### 消费者消费消息
+ 消息分发: 因为是平均派发消息，所以会导致某个机器执行很多个耗时操作
    - 可以使用channel.basicQos()  设置最大的未确认的消息来解决(类似TCP/IP的滑动窗口)
```java
/**
prefetchSize表示的是消费者未确认消息的总体大小，单位为B，设置为0表示没有上限
prefetchCount表示的是消息未确认的最大数量
global表示的是信道上的消费者按照何种策略来定义prefetchCount,false表示信道上新的消费者需要遵循，true表示信道上所有的消费者需要遵循
推荐使用global = false的配置
*/
void basicQos(int prefetchSize,int prefetchCount,boolean global);
```

+ 消息的顺序性
    - 使用basic.Recover命令剞劂
    - 自定义序列号实现
+ 消息传输保障: 最多一次、最少一次、恰好一次
    - 需要借助的机制: 事务机制或者publisher confirm机制
    - 生产者配合使用mandatory参数
    - 消息和队列都需要持久化处理
    - 消息和队列都需要设置autoAck为false,手动确认消息是否被正常消费

## RabbitMQ管理

### 用户权限
+ v host :  本质上每一个v host都是一个独立的RabbitMQ的服务器。拥有自己的队列、交换机、绑定关系，不同v host的是绝对的物理隔离
    - 使用命令rabbitmqctl add_vhost {vhost} 来创建一个新的vhost
    - rabbitmqctl delete_vhost {vhost}
    - rabbitmqctl list_vhosts
