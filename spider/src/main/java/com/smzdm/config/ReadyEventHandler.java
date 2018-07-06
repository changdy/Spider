package com.smzdm.config;

import com.smzdm.service.TopicMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReadyEventHandler {
    @Autowired
    private TopicMessageListener messageListener;
    @Autowired
    private ProjectConfig projectConfig;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void handleApplicationReady() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(stringRedisTemplate.getConnectionFactory());
        ChannelTopic channelTopic = new ChannelTopic(projectConfig.getExpiredTopic());
        container.addMessageListener(messageListener, channelTopic);
    }
}