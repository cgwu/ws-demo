package me.gg.wsdemo.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import lombok.extern.slf4j.Slf4j;
import me.gg.wsdemo.entity.UserPrincipal;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

/**
 * Created by sam on 18-12-13.
 */
@Slf4j
@Component
public class UserChannelInterceptor implements ChannelInterceptor {

    @Nullable
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(accessor.getCommand()!=null) {
            switch (accessor.getCommand()) {
                case CONNECT:
                    Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
                    String strRawHead = JSON.toJSONString(raw);
                    //ref: https://blog.csdn.net/liupeifeng3514/article/details/79180154
                    Object name = JSONPath.extract(strRawHead, "$.username[0]");
                    log.debug("Connect Frame Raw Header: {}", strRawHead);
                    log.info("username: {}", name);
                    if (name == null || !name.toString().startsWith("00")) return null;  // TODO: 测试验证
                    else {
                        // 设置当前访问器的认证用户
                        accessor.setUser(new UserPrincipal(name.toString(), "foo-token-" + name.toString()));
                    }
                    break;

                case DISCONNECT:
                    log.info("COMMAND: {}, 当前用户:{}, sessionId: {}, Id: {}", accessor.getCommand(), accessor.getUser().getName(),
                            accessor.getSessionId(), accessor.getId());
                    break;

                case SEND:
                    log.info("COMMAND: {},当前用户:{}", accessor.getCommand(), accessor.getUser().getName());
                    break;

                default:
                    break;
            }
        }
        return message;
    }

}
