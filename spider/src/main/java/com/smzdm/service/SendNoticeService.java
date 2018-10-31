package com.smzdm.service;

import com.alibaba.fastjson.JSONObject;
import com.smzdm.config.ProjectConfig;
import com.smzdm.mapper.WxNoticeResultMapper;
import com.smzdm.model.SubNoticeMsg;
import com.smzdm.pojo.WxNoticeResult;
import com.smzdm.util.HttpUtil;
import com.smzdm.util.WxMsgConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Created by Changdy on 2018/6/28.
 */
@Slf4j
@Service
public class SendNoticeService {
    @Autowired
    private ProjectConfig projectConfig;
    @Autowired
    private WxNoticeResultMapper wxNoticeResultMapper;
    @Autowired
    private ValueOperations<String, String> valueOperations;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void sendWxMsg(Integer articleId, SubNoticeMsg subNoticeMsg) {
        if (stringRedisTemplate.hasKey("send:" + articleId)) {
            return;
        }
        valueOperations.set("send:" + articleId, LocalDateTime.now().toString(), 3, TimeUnit.DAYS);
        WxNoticeTemplate wxNoticeTemplate = new WxNoticeTemplate();
        wxNoticeTemplate.url = projectConfig.getUrlPrefix() + articleId;
        wxNoticeTemplate.data = WxMsgConvertUtil.objectConvert(subNoticeMsg);
        try {
            String requestBody = JSONObject.toJSONString(wxNoticeTemplate);
            JSONObject response = HttpUtil.sendJSONPost(requestBody, projectConfig.getNoticeUrl() + valueOperations.get(projectConfig.getAccessTokenKey()));
            boolean flag = response.getInteger("errcode") == 0;
            WxNoticeResult wxNoticeResult = new WxNoticeResult();
            wxNoticeResult.setEventTime(LocalDateTime.now());
            wxNoticeResult.setResponse(response.toJSONString());
            wxNoticeResult.setSendMsg(requestBody);
            wxNoticeResult.setSuccess(flag);
            wxNoticeResultMapper.insert(wxNoticeResult);
        } catch (IOException e) {
            log.error("发送失败", e);
            e.printStackTrace();
        }
    }


    private class WxNoticeTemplate {
        public String touser = "oVJJCvx28HDsflYiPF-EwKsYeOJ8";
        public String template_id = "fuaaUQYgNXWzX8ffT0Sz28u08f2h1Qx9aDbe-ciHazg";
        public String url;
        public Object data;
    }
}