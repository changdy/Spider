package com.smzdm.config;

import com.smzdm.enums.TypeRelationEnum;
import com.smzdm.mapper.BaseEnumMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReadyEventHandler {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private BaseEnumMapper baseEnumMapper;

    @EventListener(ApplicationReadyEvent.class)
    public void handleApplicationReady() {
        TypeRelationEnum[] enums = TypeRelationEnum.values();
        for (TypeRelationEnum value : enums) {
            String[] values = baseEnumMapper.getEnum(value.getKey().split(":")[1]);
            stringRedisTemplate.delete(value.getKey());
            stringRedisTemplate.opsForSet().add(value.getKey(), values);
        }
    }
}