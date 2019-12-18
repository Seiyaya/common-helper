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