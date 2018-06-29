package com.smzdm.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Map;

/**
 * Created by Changdy on 2018/3/20.
 */
@Slf4j
public class HttpUtil {

    public static <T> T sendJSONPost(String requestBody, String url, Class<T> returnType) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(requestBody, "utf-8");
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-type", " application/json; charset=utf-8");
        log.info("请求信息:" + MessageFormat.format("url:{0},parameter:{1}", url, requestBody));
        return generatorBody(httpClient, httpPost, returnType);
    }

    public static JSONObject sendJSONPost(String requestBody, String url) throws IOException {
        return sendJSONPost(requestBody, url, JSONObject.class);
    }

    public static <T> T sendGet(String url, Class<T> returnType) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        return generatorBody(httpClient, httpGet, returnType);
    }

    private static <T> T generatorBody(CloseableHttpClient httpClient, HttpRequestBase httpRequestBase, Class<T> returnType) throws IOException {
        T t;
        CloseableHttpResponse httpResponse = httpClient.execute(httpRequestBase);
        String response = EntityUtils.toString(httpResponse.getEntity());
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            log.info("返回值:" + response);
            t = JSON.parseObject(response, returnType);
        } else {
            log.error("请求失败,返回结果" + statusCode);
            log.error("返回值:" + response);
            throw new RuntimeException(response);
        }
        httpResponse.close();
        httpClient.close();
        return t;
    }

    public static <T> T doGET(String url, Class<T> returnType) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        return generatorBody(client, request, returnType);
    }

    public static <T> T doGET(String url, Class<T> returnType, Map<String, String> map) throws IOException {
        String param = generateParam(map);
        if (param != null) {
            if (url.lastIndexOf('?') == url.length() - 1) {
                url += url + param;
            } else {
                url += url + '?' + param;
            }
        }
        return doGET(url, returnType);
    }

    public static String generateParam(Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        map.forEach((k, v) -> {
            try {
                builder.append(k).append('=').append(URLEncoder.encode(v, "UTF-8")).append('&');
            } catch (UnsupportedEncodingException e) {
                log.error("编码失败:" + k, e);
                throw new RuntimeException(e);
            }
        });
        return builder.deleteCharAt(builder.length() - 1).toString();
    }
}
