package com.smzdm.service;

import com.smzdm.config.ProjectConfig;
import com.smzdm.mapper.ArticleMapper;
import com.smzdm.pojo.ArticleInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

@Slf4j
@Component
public class TopicMessageListener implements MessageListener {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private PageProcessor htmlProcessor;

    @Autowired
    private ProjectConfig projectConfig;

    @Override
    public void onMessage(Message message, byte[] pattern) {// 客户端监听订阅的topic，当有消息的时候，会触发该方法
        // sub:9958301-30
        String key = new String(message.getBody());
        if (key.startsWith("sub:")) {
            String[] split = key.replace("sub:", "").split("-");
            Integer articleId = Integer.valueOf(split[0]);
            Integer worth = Integer.valueOf(split[1]);
            Spider spider = Spider.create(htmlProcessor);
            ResultItems resultItems = spider.get(projectConfig.getArticleUrl() + articleId);
            ArticleInfo info = resultItems.get("ArticleInfo");
            spider.close();
        }
    }
}