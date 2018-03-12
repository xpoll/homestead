package cn.blmdz.home.services.manager;

import org.springframework.web.socket.WebSocketSession;

import cn.blmdz.home.base.BaseUser;
import cn.blmdz.home.model.msg.BaseMsg;

public interface SocketManager {

    void connect(BaseUser user, WebSocketSession session);
    
    void message(BaseUser user, BaseMsg baseMsg);
    
    void close(BaseUser user);
    
    void error(BaseUser user);
}
