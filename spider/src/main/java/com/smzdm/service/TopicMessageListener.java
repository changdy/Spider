package com.smzdm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TopicMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {// 客户端监听订阅的topic，当有消息的时候，会触发该方法
        byte[] body = message.getBody();// 请使用valueSerializer
        byte[] channel = message.getChannel();
        String topic = new String(channel);
        String itemValue = new String(body);
        // 请参考配置文件，本例中key，value的序列化方式均为string。
        log.info("topic:" + topic);
        log.info("itemValue:" + itemValue);
    }
}