package com.smzdm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by Changdy on 2018/3/8.
 *
 * 为了解决redis操作乱码
 */
@Configuration
public class RedisTemplateConfig {
    private RedisSerializer objectSerializer = new GenericJackson2JsonRedisSerializer();
    private RedisSerializer stringSerializer = new StringRedisSerializer();

    @Resource
    private RedisTemplate<String, Long> redisTemplate;

    @PostConstruct
    public void init() {
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(objectSerializer);
    }
}
