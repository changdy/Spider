package com.smzdm.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smzdm.pojo.ArticleInfo;
import com.smzdm.util.HttpUtil;
import com.smzdm.util.WxMsgConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Changdy on 2018/6/28.
 */
@Slf4j
@Service
public class SendNoticeService {
    @Value("${custom.access-token-key}")
    private String redisToken;
    @Value("${custom.url-prefix}")
    private String urlPrefix;
    @Value("${custom.notice-url}")
    private String noticeUrl;

    @Autowired
    private ValueOperations<String, String> valueOperations;

    public void sendWxMsg(Integer articleId, ArticleInfo articleInfo) {
        WxNoticeTemplate wxNoticeTemplate = new WxNoticeTemplate();
        wxNoticeTemplate.url = urlPrefix + articleId;
        wxNoticeTemplate.data = WxMsgConvertUtil.objectConvert(articleId);
        try {
            String requestBody = JSONObject.toJSONString(wxNoticeTemplate);
            JSONObject response = HttpUtil.sendJSONPost(requestBody, noticeUrl + valueOperations.get(redisToken));
        } catch (IOException e) {
            log.error("发送失败", e);
            e.printStackTrace();
        }
    }


    private class WxNoticeTemplate {
        public String touser = "oVJJCvx28HDsflYiPF-EwKsYeOJ8";
        public String template_id = "Az1xsobT6jQvc3TOl25ekpNuRPGHdu5pMjgBXRYLlrk";
        public String url;
        public Object data;
    }
}