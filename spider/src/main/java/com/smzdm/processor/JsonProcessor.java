package com.smzdm.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by Changdy on 2017/8/18.
 */
@Service
public class JsonProcessor implements PageProcessor {
    private static final Site SITE = Site.me().setRetryTimes(5).setSleepTime(1500).setUseGzip(true).setRetrySleepTime(5000);

    @Override
    public void process(Page page) {
        Object parse = JSON.parse(page.getRawText());
        if (parse instanceof JSONObject) {
            page.putField("json", ((JSONObject) parse).getJSONArray("data"));
        } else if (parse instanceof JSONArray) {
            page.putField("json", parse);
        }
    }

    @Override
    public Site getSite() {
        return SITE;
    }
}
