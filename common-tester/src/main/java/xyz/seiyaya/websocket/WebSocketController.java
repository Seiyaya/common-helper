package xyz.seiyaya.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.seiyaya.common.cache.helper.StringHelper;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/4/2 15:28
 */
@ServerEndpoint("/webSocket/{userId}")
@Slf4j
@Component
public class WebSocketController {


    private Map<String,Session> webSocketSession = new ConcurrentHashMap<>();

    private AtomicInteger onlineCount = new AtomicInteger(0);

    @OnMessage
    public String hello(String msg){
        log.info("received msg:{}" , msg);
        return msg;
    }


    /**
     *  @OnOpen标注的方法在WebSocket连接开始时被调用
     * @param session
     */
    @OnOpen
    public void myOnOpen(Session session , @PathParam("userId") String userId){
        if(StringHelper.isNotEmpty(userId)){
            webSocketSession.put(userId , session);
            onlineCount.incrementAndGet();
            log.info("online person :{}",onlineCount.get());
        }
        log.info("web socket open,session id :{}",session.getId());
    }

    /**
     * @OnClose标注的方法在连接关闭时被调用。
     * @param reason
     */
    @OnClose
    public void myOnClose(CloseReason reason,@PathParam("userId") String userId){
        log.info("close a websocket due to :{}",reason.getReasonPhrase());
        onlineCount.getAndDecrement();
        webSocketSession.remove(userId);
        log.info("online person :{}",onlineCount.get());
    }

}
