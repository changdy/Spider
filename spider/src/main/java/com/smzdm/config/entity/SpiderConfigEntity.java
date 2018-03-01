package com.smzdm.config.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spider")
public class SpiderConfigEntity {
    private String[] homeUrl;
    private String[] discoveryUrl;
    private String[] hotUrl;
}