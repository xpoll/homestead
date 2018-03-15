package cn.blmdz.home.services.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import cn.blmdz.home.base.BaseUser;
import cn.blmdz.home.model.msg.BaseMsg;
import cn.blmdz.home.services.manager.SocketManager;
import cn.blmdz.home.util.JsonMapper;

/**
 * socket Helper
 * @author yongzongyang
 * @date 2018年1月26日
 */
@Component
public class SocketJsHelper implements WebSocketHandler {
    
    @Autowired
    private SocketManager socketManager;
    
    
    /* 
     * 接入SOCKET
     * @see org.springframework.web.socket.WebSocketHandler#afterConnectionEstablished(org.springframework.web.socket.WebSocketSession)
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("接入SOCKET");
        BaseUser user = getUser(session);
        if (user == null) return;
        
        socketManager.connect(user, session);
    }

    /* 发送消息
     * @see org.springframework.web.socket.WebSocketHandler#handleMessage(org.springframework.web.socket.WebSocketSession, org.springframework.web.socket.WebSocketMessage)
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println("发送消息");
        BaseUser user = getUser(session);
        if (user == null) return;
        
        socketManager.message(user, JsonMapper.nonDefaultMapper().fromJson(message.getPayload().toString(), BaseMsg.class));
    }

    /* 链接关闭
     * @see org.springframework.web.socket.WebSocketHandler#afterConnectionClosed(org.springframework.web.socket.WebSocketSession, org.springframework.web.socket.CloseStatus)
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus cs) throws Exception {
        System.out.println("链接关闭");
        BaseUser user = getUser(session);
        if (user == null) return;

        socketManager.close(getUser(session));
    }

    /* 链接错误
     * @see org.springframework.web.socket.WebSocketHandler#handleTransportError(org.springframework.web.socket.WebSocketSession, java.lang.Throwable)
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable thrwbl) throws Exception {
        System.out.println("链接错误");
        if (session.isOpen()) session.close();
        BaseUser user = getUser(session);
        if (user == null) return;
        
        socketManager.error(getUser(session));
    }

    @Override
    public boolean supportsPartialMessages() {
        System.out.println("supportsPartialMessages");
        return false;
    }
    
    private static BaseUser getUser(WebSocketSession session) {
        if (session != null && session.getAttributes().get("user") != null) return (BaseUser) session.getAttributes().get("user");
        return null;
    }
}
