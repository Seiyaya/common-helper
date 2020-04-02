package xyz.seiyaya.websocket;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/4/2 15:28
 */
@ServerEndpoint("/webSocket")
@Slf4j
public class WebSocketController {


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
    public void myOnOpen(Session session){
        log.info("web socket open,session id :{}",session.getId());
    }

    /**
     * @OnClose标注的方法在连接关闭时被调用。
     * @param reason
     */
    @OnClose
    public void myOnClose(CloseReason reason){
        log.info("close a websocket due to :{}",reason.getReasonPhrase());
    }

}
