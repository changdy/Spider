package com.smzdm.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.smzdm.mapper.ArticleMapper;
import com.smzdm.model.*;
import com.smzdm.util.ResultUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Changdy on 2018/3/26.
 */
@RestController
@RequestMapping("/article")
public class ArticleListController {

    @Value("${custom.search-url}")
    private String url;

    @Autowired
    private ArticleMapper articleMapper;


    @PostMapping("/proxy-list")
    public ResponseResult<List<ArticleModel>> getList(@RequestBody ApiSearchModel apiSearchModel) throws IOException, URISyntaxException {
        String type = apiSearchModel.isSearchByCategory() ? "faxian" : "home";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet();
        httpGet.setHeader("User-Agent", "smzdm_android_V8.7.4 rv:450 (ZUK Z2131;Android8.0.0;zh)smzdmapp");
        httpGet.setHeader("Host", "api.smzdm.com");
        List<ArticleModel> list = new ArrayList<>();
        for (Integer i = 0; i < apiSearchModel.getPage(); i++) {
            list.addAll(sendRequest(httpClient, MessageFormat.format(url, apiSearchModel.getKeyWords(), type, i * 100), httpGet));
        }
        httpClient.close();
        return ResultUtil.success(list);
    }

    private List<ArticleModel> sendRequest(CloseableHttpClient httpClient, String uri, HttpGet httpGet) throws IOException, URISyntaxException {
        httpGet.setURI(new URI(uri));
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        List<ArticleModel> articleModels = null;
        if (execute.getStatusLine().getStatusCode() == 200) {
            JSONArray jsonArray = JSON.parseObject(EntityUtils.toString(execute.getEntity())).getJSONObject("data").getJSONArray("rows");
            articleModels = jsonArray.stream().map(x -> JSON.parseObject(JSON.toJSONString(x), ArticleModel.class)).collect(Collectors.toList());
        }
        execute.close();
        return articleModels;
    }

    @PostMapping("/search-article")
    public ResponseResult<SimplePageContent<ArticleModel>> getList(@RequestBody DataBaseSearchModel model) {
        return ResultUtil.success(new SimplePageContent<>(articleMapper.getCount(model), articleMapper.queryArticle(model)));
    }
}
