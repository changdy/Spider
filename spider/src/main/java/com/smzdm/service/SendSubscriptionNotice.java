package com.smzdm.service;

import com.alibaba.fastjson.JSON;
import com.smzdm.model.SubNoticeMsg;
import com.smzdm.pojo.Article;
import com.smzdm.pojo.ArticleInfo;
import com.smzdm.pojo.ArticleSubscription;
import com.smzdm.pojo.Category;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Changdy on 2018/6/28.
 */
@Service
public class SendSubscriptionNotice {
    @Autowired
    private ValueOperations<String, String> valueOperations;

    @Autowired
    private List<ArticleSubscription> articleSubscriptions;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${custom.sub-prefix}")
    private String subPrefix;
    @Autowired
    private List<Category> categories;
    @Autowired
    private SendNoticeService sendNoticeService;

    // 检查是否已经 符合规则
    public void checkArticle(List<Article> articles) {
        articleSubscriptions.forEach(subscription -> articles.forEach(article -> {
            if (check(article, subscription)) {
                SubNoticeMsg subNoticeMsg = new SubNoticeMsg();
                subNoticeMsg.setMall(article.getMall());
                subNoticeMsg.setPrice(article.getPrice());
                subNoticeMsg.setTitle(article.getTitle());
                List<String> category = new ArrayList<>();
                for (short categoryId : article.getCategory()) {
                    categories.stream().filter(x -> x.getId() == categoryId).findFirst().map(Category::getTitle).ifPresent(category::add);
                }
                subNoticeMsg.setCategory(String.join(",", category));
                valueOperations.set(subPrefix + article.getArticleId() + "-" + subscription.getWorthCount(), String.valueOf(subscription.getWorthCount()), 6, TimeUnit.HOURS);
            }
        }));
    }

    // 检查是否 满足点赞条件
    public void checkInfo(List<ArticleInfo> infoList) {
        Set<String> keys = stringRedisTemplate.keys(subPrefix + "*");
        keys.forEach(key -> {
            int articleId = Integer.valueOf(key.split(":")[1].split("-")[0]);
            int worth = Integer.valueOf(key.split(":")[1].split("-")[1]);
            infoList.forEach(info -> {
                if (info.getArticleId() == articleId && info.getWorthy() > worth) {
                    stringRedisTemplate.delete(key);
                    SubNoticeMsg subNoticeMsg = JSON.parseObject(valueOperations.get(key), SubNoticeMsg.class);
                    String appraise = MessageFormat.format("值:{0},不值:{1},评论:{2},收藏", info.getWorthy(), info.getUnworthy(), info.getComment(), info.getCollection());
                    subNoticeMsg.setAppraise(appraise);
                    sendNoticeService.sendWxMsg(articleId, subNoticeMsg);
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
}
