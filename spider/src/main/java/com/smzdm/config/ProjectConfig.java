package com.smzdm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Changdy on 2018/7/5.
 */
@Data
@Component
@ConfigurationProperties("project-config")
public class ProjectConfig {
    private String turn;
    private String unknownCategory;
    private String categoryKey;
    private String subPrefix;
    private String accessTokenKey;
    private String openId;
    private String urlPrefix;
    private String noticeUrl;
    private String ipUrl;
    private String ipKey;
    private String articleUrl;
    private String expiredTopic;
}
