package me.gg.wsdemo.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by danny on 2018/12/12.
 */
@Slf4j
@Component("webSocketProducer")
public class WebSocketProducer {
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private SimpMessagingTemplate template;

    public void sendMessageTo(String topic, String message) {
        log.info("产生消息,topic:{},msg:{}", topic, message);
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(dateFormatter.format(new Date()));
        builder.append("]");
        builder.append(message);
        this.template.convertAndSend("/topic/" + topic, builder.toString());
    }

}
