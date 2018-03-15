package cn.blmdz.home.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocketMessageBroker// 开启使用STOMP协议来传输基于代理的消息，Broker就是代理的意思
@EnableWebSocket
public class WebSocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer implements WebSocketConfigurer {
    
    @Autowired
    private WebSocketHandler socketJsHelper;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/simple"); // 订阅代理
        config.setApplicationDestinationPrefixes("/app"); //发送消息前缀
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {// StompEndpointRegistry 注册STOMP协议的节点，并指定映射的URL
        registry.addEndpoint("/endpoint/placard").setAllowedOrigins("*").withSockJS(); // 这一行代码用来注册STOMP协议节点，同时指定使用SockJS协议。
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketJsHelper, "/endpoint/deal").setAllowedOrigins("*").withSockJS().setInterceptors(new WebSocketInterceptor());
        registry.addHandler(socketJsHelper, "/endpoint/deal/applet").setAllowedOrigins("*").addInterceptors(new WebSocketInterceptor());
    }

    
    
}