package me.gg.wsdemo.web;

import lombok.extern.slf4j.Slf4j;
import me.gg.wsdemo.component.WebSocketProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by danny on 2018/12/12.
 */
@Controller
@RequestMapping("/websocket")
@MessageMapping("ws")
@Slf4j
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private WebSocketProducer producer;

    /* HTTP请求方法 */
    @RequestMapping("/msg-list/{chanel}")
    public String msgList(@PathVariable String chanel, HttpServletRequest request) {
        request.setAttribute("userChanel", chanel);
        return "websocket/msg-list";
    }

    @RequestMapping("/send/{topic}")
    @ResponseBody
    public String send(@PathVariable String topic, @RequestParam String message) {
        producer.sendMessageTo(topic, message);
        return "OK-Sent";
    }

    /* WebSocket请求方法 */
    @MessageMapping("bar.{baz}")
    public void handleBaz(@DestinationVariable String baz) {
        log.info("服务器接收到WebSocket消息: $baz");
    }

    @MessageMapping("logbody")
    public void logMsg(@Payload String body, @Header("priority") String priority,
                       @Headers Map<String, Object> headers) {
        log.info("服务器接收到WebSocket消息体内容: $body");
        log.info("优先级: $priority");
        log.info("所有头部: $headers");
        producer.sendMessageTo("user1", body);
    }

    // 1.x测试发现: @DestinationVariable 和 @Header,@Payload 不能同时使用,否则前者取值错误.
    @MessageMapping("user")
    public void sendtouser(@Header("userId") String userId, @Payload String body) {
        log.info("发送消息到用户: $userId, Msg: $body");
        // 此类消息客户端应使用的订阅路径template为: /user/{userId}/msg
        template.convertAndSendToUser(userId, "/msg", body);
    }

    @MessageMapping("topic")
    public void broadcast(@Payload String body) {
        log.info("广播消息: $body");
        template.convertAndSend("/topic", body);
    }

}
