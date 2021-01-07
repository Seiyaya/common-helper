package xyz.seiyaya.base.http;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * 使用连接池     278ms   41ms   46ms   38ms   42ms
 * 不实用连接池   286ms   141ms   118ms   81ms   99ms
 * @author wangjia
 * @version v1.0
 * @date 2021/1/7 14:00
 */
@SuppressWarnings("all")
public class ConnectionPoolTest extends BaseHttpClientTest {

    /**
     * http请求过程
     * 域名解析 --> tcp三次握手 --> 客户端发送httpRequest -->  服务端响应httpRequest --> 客户端解析、处理httpResponse --> tcp四次挥手
     * http1.0 每次请求都是单独的连接，上一次和下一次完全隔离
     * http1.1 支持在一个tcp连接上传送多个http请求和响应，减少了建立和关闭连接的消耗延迟
     *      request-header:  Connection:keep-alive   Keep-alive:timeout=5,max=100       // 最多保持5s，长连接接收100次就自动断掉
     *      reponse-header:  Connection:keep-alive
     *      reponse-header:  Connection:close
     * http的keep-alive： 复用已有连接
     * tcp的keep-alive: 是为了保活，保证断对端存活
     *
     * https大致原理: 在tcp和http层添加了一个ssl/tls层，对报文进行加密
     * 证书交换-->服务端加密-->客户端解密
     * 如何解决会话复用的问题，避免重复进行加解密
     * 1. sessionId会话复用，对于已经建立的tls会话，使用sessionId作为key,主密钥为value组成键值对保存在客户端和服务端
     *    第二次握手时，客户端想复用会话发起请求时带上sessionId,服务端校验是否存在sessionId进行复用会话
     *    缺点就是负载均衡中多机的session同步问题
     * 2. sessionTicket: 加密的数据blob,其中包含需重用的tls信息和sessionKey，它一般使用ticketKey加密
     *    服务端发送它到客户端，客户端后面请求带上这个ticketKey，一个session ticket超时时间默认为300s
     *
     * 使用连接池每个任务的连接时间不要过长
     */

    /**
     * 不使用连接池测试
     */
    @Test
    public void noUseConnectionPool(){
        startUpAllThreads(getRunThreads(()->{
            /**
             * HttpClient是线程安全的，因此HttpClient正常使用应当做成全局变量，但是一旦全局共用一个，HttpClient内部构建的时候会new一个连接池
             * 出来，这样就体现不出使用连接池的效果，因此这里每次new一个HttpClient，保证每次都不通过连接池请求对端
             */
            // 286ms   141ms   118ms   81ms   99ms
            CloseableHttpClient httpClient = HttpClients.custom().build();
            HttpGet httpGet = new HttpGet("https://www.baidu.com");
            long start = System.currentTimeMillis();

            try {
                CloseableHttpResponse response = httpClient.execute(httpGet);
                if(response != null){
                    response.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                addCost(System.currentTimeMillis() - start);

                if(NOW_COUNT.incrementAndGet() == REQUEST_COUNT){
                    System.out.println(EVERY_REQ_COST.toString());
                }
            }
        }));
    }

    @Test
    public void useConnectionPool(){
        CloseableHttpClient httpClient = initHttpClient();
        startUpAllThreads(getRunThreads(()->{
            HttpGet httpGet = new HttpGet("https://www.baidu.com");
            long start = System.currentTimeMillis();
            try {
                CloseableHttpResponse response = httpClient.execute(httpGet);
                if(response != null){
                    response.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                addCost(System.currentTimeMillis() - start);

                if(NOW_COUNT.incrementAndGet() == REQUEST_COUNT){
                    System.out.println(EVERY_REQ_COST.toString());
                }
            }
        }));
    }

    /**
     * 初始化连接池并得到httpclient
     */
    private CloseableHttpClient initHttpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        // 总连接池数量
        connectionManager.setMaxTotal(1);
        // 可为每个域名设置单独的连接池数量
        connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("www.baidu.com")), 1);
        // setConnectTimeout表示设置建立连接的超时时间
        // setConnectionRequestTimeout表示从连接池中拿连接的等待超时时间
        // setSocketTimeout表示发出请求后等待对端应答的超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).setConnectionRequestTimeout(2000)
                .setSocketTimeout(3000).build();
        // 重试处理器，StandardHttpRequestRetryHandler这个是官方提供的，看了下感觉比较挫，很多错误不能重试，可自己实现HttpRequestRetryHandler接口去做
        HttpRequestRetryHandler retryHandler = new StandardHttpRequestRetryHandler();

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig)
                .setRetryHandler(retryHandler).build();

        // 服务端假设关闭了连接，对客户端是不透明的，HttpClient为了缓解这一问题，在某个连接使用前会检测这个连接是否过时，如果过时则连接失效，但是这种做法会为每个请求
        // 增加一定额外开销，因此有一个定时任务专门回收长时间不活动而被判定为失效的连接，可以某种程度上解决这个问题
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    // 关闭失效连接并从连接池中移除
                    connectionManager.closeExpiredConnections();
                    // 关闭30秒钟内不活动的连接并从连接池中移除，空闲时间从交还给连接管理器时开始
                    connectionManager.closeIdleConnections(20, TimeUnit.SECONDS);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }, 0 , 1000 * 5);


        return httpClient;
    }
}
