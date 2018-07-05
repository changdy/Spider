package com.smzdm.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smzdm.config.ProjectConfig;
import com.smzdm.model.SubNoticeMsg;
import com.smzdm.pojo.Article;
import com.smzdm.pojo.ArticleInfo;
import com.smzdm.pojo.ArticleSubscription;
import com.smzdm.pojo.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Changdy on 2018/6/28.
 */
@Slf4j
@Service
public class SendSubscriptionNotice {
    @Autowired
    private ValueOperations<String, String> valueOperations;

    @Autowired
    private List<ArticleSubscription> articleSubscriptions;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ProjectConfig projectConfig;
    @Autowired
    private List<Category> categories;
    @Autowired
    private SendNoticeService sendNoticeService;

    // 检查是否已经 符合规则
    public void checkArticle(List<Article> articles) {
        articleSubscriptions.forEach(subscription -> articles.forEach(article -> {
            if (check(article, subscription)) {
                SubNoticeMsg subNoticeMsg = generateSubNoticeMsg(article);
                String key = projectConfig.getSubPrefix() + article.getArticleId() + "-" + subscription.getWorthCount();
                valueOperations.set(key, JSONObject.toJSONString(subNoticeMsg), 6, TimeUnit.HOURS);
            }
        }));
    }

    // 检查是否 满足点赞条件
    @Async
    public void checkInfo(List<ArticleInfo> infoList) {
        Set<String> keys = stringRedisTemplate.keys(projectConfig.getSubPrefix() + "*");
        keys.forEach(key -> {
            int articleId = Integer.valueOf(key.split(":")[1].split("-")[0]);
            int worth = Integer.valueOf(key.split(":")[1].split("-")[1]);
            infoList.forEach(info -> {
                if (info.getArticleId() == articleId && info.getWorthy() > worth) {
                    SubNoticeMsg subNoticeMsg = JSON.parseObject(valueOperations.get(key), SubNoticeMsg.class);
                    subNoticeMsg.setAppraise(generateAppraise(info));
                    sendNoticeService.sendWxMsg(articleId, subNoticeMsg);
                    stringRedisTemplate.delete(key);
                }
            });
        });
    }


    private boolean check(Article article, ArticleSubscription articleSubscription) {
        String title = article.getTitle();
        if (articleSubscription.getTitleContain() != null) {
            for (String s : articleSubscription.getTitleContain()) {
                if (!title.contains(s)) {
                    return false;
                }
            }
        }
        if (articleSubscription.getTitleReject() != null) {
            for (String s : articleSubscription.getTitleReject()) {
                if (title.contains(s)) {
                    return false;
                }
            }
        }
        Short[] category = article.getCategory();
        if (articleSubscription.getCategoryReject() != null) {
            for (short s : articleSubscription.getCategoryReject()) {
                for (Short articleCategoryItem : category) {
                    if (articleCategoryItem == s) {
                        return false;
                    }
                }
            }
        }
        if (articleSubscription.getCategoryContain() != null) {
            for (short temp : articleSubscription.getCategoryContain()) {
                for (short articleCategoryItem : category) {
                    if (articleCategoryItem == temp) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }


    public SubNoticeMsg generateSubNoticeMsg(Article article) {
        SubNoticeMsg subNoticeMsg = new SubNoticeMsg();
        subNoticeMsg.setMall(article.getMall());
        subNoticeMsg.setPrice(article.getPrice());
        subNoticeMsg.setTitle(article.getTitle());
        if (article.getCategory() != null) {
            List<String> category = new ArrayList<>();
            for (short categoryId : article.getCategory()) {
                categories.stream().filter(x -> x.getId() == categoryId).findFirst().map(Category::getTitle).ifPresent(category::add);
            }
            subNoticeMsg.setCategory(String.join(",", category));
        } else {
            subNoticeMsg.setCategory("");
        }
        return subNoticeMsg;
    }

    public String generateAppraise(ArticleInfo info) {
        return MessageFormat.format("值:{0},不值:{1},评论:{2},收藏:{3}", info.getWorthy(), info.getUnworthy(), info.getComment(), info.getCollection());
    }
}