package com.smzdm.scheduling;

import com.smzdm.config.ProjectConfig;
import com.smzdm.mapper.ArticleSubscriptionMapper;
import com.smzdm.model.ArticleModel;
import com.smzdm.model.SubNoticeMsg;
import com.smzdm.pojo.ArticleSubscription;
import com.smzdm.service.SendNoticeService;
import com.smzdm.util.SendRequestUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
    @Autowired
    private SendNoticeService sendNoticeService;

    private volatile int turn = 0;


    @Scheduled(fixedDelay = 1000 * 60 * 60)
    public void updateSubscription() {
        articleSubscriptions.clear();
        articleSubscriptions.addAll(articleSubscriptionMapper.selectAll());
    }


    @Scheduled(fixedDelay = 1000 * 60 * 6)
    public void checkByUrl() throws IOException, URISyntaxException {
        LocalTime now = LocalTime.now();
        if (now.getHour() < 7 && now.getSecond() < 45) {
            return;
        }
        if (turn >= articleSubscriptions.size()) {
            turn = 0;
        } else {
            turn = turn + 1;
        }
        ArticleSubscription articleSubscription = articleSubscriptions.get(turn);
        Short[] categoryContain = articleSubscription.getCategoryContain();
        if (categoryContain != null && categoryContain.length > 0) {
            String url = "https://api.smzdm.com/v1/list?keyword=" + categoryContain[0] + "&type=faxian&order=time&limit=80&offset=0";
            List<ArticleModel> articleModels = SendRequestUtil.sendRequest(url);
            List<ArticleModel> collect = articleModels.stream().filter(x -> x.getWorthy() > 15).collect(toList());
            if (!CollectionUtils.isEmpty(collect)) {
                for (ArticleModel articleModel : collect) {
                    SubNoticeMsg subNoticeMsg = new SubNoticeMsg();
                    subNoticeMsg.setMall(articleModel.getMall());
                    subNoticeMsg.setTitle(articleModel.getTitle());
                    subNoticeMsg.setPrice(articleModel.getPrice());
                    String appraise = "值:" + articleModel.getWorthy() + " 不值:" + articleModel.getUnworthy() + " 评论:" + articleModel.getComment() + " 收藏:" + articleModel.getCollection();
                    subNoticeMsg.setAppraise(appraise);
                    subNoticeMsg.setCategory(articleModel.getCategoryTitle());
                    sendNoticeService.sendWxMsg(articleModel.getArticleId(), subNoticeMsg);
                }
            }
        }
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
