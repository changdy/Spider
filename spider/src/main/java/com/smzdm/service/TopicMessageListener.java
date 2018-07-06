package com.smzdm.service;

import com.alibaba.fastjson.JSON;
import com.smzdm.config.ProjectConfig;
import com.smzdm.mapper.ArticleMapper;
import com.smzdm.model.SubNoticeMsg;
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
    @Autowired
    private SendSubscriptionNotice sendSubscriptionNotice;
    @Autowired
    private SendNoticeService sendNoticeService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = new String(message.getBody());
        log.info("{}过期", key);
        if (key.startsWith("sub:")) {
            String[] split = key.replaceFirst("sub:", "").split("-");
            Integer articleId = Integer.valueOf(split[0]);
            Short worth = Short.valueOf(split[1]);
            Spider spider = Spider.create(htmlProcessor);
            ResultItems resultItems = spider.get(projectConfig.getArticleUrl() + articleId);
            ArticleInfo info = resultItems.get("ArticleInfo");
            log.info("最新点赞情况如{}", JSON.toJSONString(info));
            if (info.getWorthy() > worth) {
                SubNoticeMsg subNoticeMsg = sendSubscriptionNotice.generateSubNoticeMsg(articleMapper.getMainInfo(articleId));
                subNoticeMsg.setAppraise(sendSubscriptionNotice.generateAppraise(info));
                sendNoticeService.sendWxMsg(articleId, subNoticeMsg);
            }
            spider.close();
        }
    }
}