package com.smzdm.scheduling;

import com.smzdm.config.ProjectConfig;
import com.smzdm.mapper.ArticleSubscriptionMapper;
import com.smzdm.pojo.ArticleSubscription;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Created by Changdy on 2018/7/3.
 */
@Component
public class OtherTask {

    @Autowired
    private List<ArticleSubscription> articleSubscriptions;
    @Autowired
    private ArticleSubscriptionMapper articleSubscriptionMapper;
    @Autowired
    private ProjectConfig projectConfig;
    @Autowired
    private ValueOperations<String, String> valueOperations;


    @Scheduled(fixedDelay = 1000 * 60 * 60, initialDelay = 1000 * 60 * 60)
    public void updateSubscription() {
        articleSubscriptions.clear();
        articleSubscriptions.addAll(articleSubscriptionMapper.selectAll());
    }

    @Scheduled(fixedDelay = 1000 * 60 * 15)
    public void updateIpAddress() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(projectConfig.getIpUrl());
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            valueOperations.set(projectConfig.getIpKey(), EntityUtils.toString(httpResponse.getEntity()));
        }
        httpResponse.close();
        httpClient.close();
    }
}
