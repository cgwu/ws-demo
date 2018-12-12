package me.gg.wsdemo.config;

import me.gg.wsdemo.component.MyWsHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * ref: https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#websocket
 * STOMP: Simple (or Streaming) Text Orientated Messaging Protocol.
 */
@Configuration
@EnableWebSocketMessageBroker
public class StompOnWebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private MyWsHandshakeInterceptor myWsHandshakeInterceptor;

    private TaskScheduler messageBrokerTaskScheduler;

    @Autowired
    public void setMessageBrokerTaskScheduler(TaskScheduler messageBrokerTaskScheduler) {
        this.messageBrokerTaskScheduler = messageBrokerTaskScheduler;
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp")
                .setAllowedOrigins("*").addInterceptors(myWsHandshakeInterceptor)
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic", "/user")
                .setHeartbeatValue(new long[]{5000, 5000})  // 设置心跳
                .setTaskScheduler(this.messageBrokerTaskScheduler);
        registry.setPathMatcher(new AntPathMatcher("."));
        //        registry.setUserDestinationPrefix("/user"); // default: "/user/"
    }

}


