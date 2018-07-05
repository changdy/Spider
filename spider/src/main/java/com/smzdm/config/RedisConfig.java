package com.smzdm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Changdy on 2018/4/14.
 */
@Data
@Component
@ConfigurationProperties("redis-config")
public class RedisConfig {
    private String expiredTopic;
    private Integer corePoolSize;
    private Integer maxPoolSize;
    private Integer queueCapacity;
    private Integer keepAliveSeconds;
    private String threadNamePrefix;
}
