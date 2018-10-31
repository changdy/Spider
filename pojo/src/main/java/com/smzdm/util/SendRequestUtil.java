package com.smzdm.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.smzdm.model.ArticleModel;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Changdy on 2018/10/31.
 */
public class SendRequestUtil {
    public static List<ArticleModel> sendRequest(String uri) throws IOException, URISyntaxException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet();
        httpGet.setHeader("User-Agent", "smzdm_android_V8.7.4 rv:450 (ZUK Z2131;Android8.0.0;zh)smzdmapp");
        httpGet.setHeader("Host", "api.smzdm.com");
        httpGet.setURI(new URI(uri));
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        List<ArticleModel> articleModels = null;
        if (execute.getStatusLine().getStatusCode() == 200) {
            JSONArray jsonArray = JSON.parseObject(EntityUtils.toString(execute.getEntity())).getJSONObject("data").getJSONArray("rows");
            articleModels = jsonArray.stream().map(x -> JSON.parseObject(JSON.toJSONString(x), ArticleModel.class)).collect(Collectors.toList());
        }
        execute.close();
        httpClient.close();
        return articleModels;
    }
}
