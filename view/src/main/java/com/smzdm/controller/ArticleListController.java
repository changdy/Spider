package com.smzdm.controller;

import com.alibaba.fastjson.JSON;
import com.smzdm.mapper.ArticleMapper;
import com.smzdm.model.ArticleModel;
import com.smzdm.model.ArticleSearch;
import com.smzdm.model.ResponseResult;
import com.smzdm.model.SearchType;
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

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Changdy on 2018/3/26.
 */
@RestController
@RequestMapping("/article")
public class ArticleListController {

    @Autowired
    private ArticleMapper articleMapper;
    @Value("${custom.search-url}")
    private String url;


    @PostMapping("proxy-list")
    public ResponseResult<List<ArticleModel>> getList(@RequestBody SearchType searchType) throws IOException, URISyntaxException {
        String type = searchType.isSearchByCategory() ? "faxian" : "home";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet();
        httpGet.setHeader("User-Agent", "smzdm_android_V8.7.4 rv:450 (ZUK Z2131;Android8.0.0;zh)smzdmapp");
        httpGet.setHeader("Host", "api.smzdm.com");
        List<ArticleModel> list = new ArrayList<>();
        for (Integer i = 0; i < searchType.getPage(); i++) {
            list.addAll(sendRequest(httpClient, MessageFormat.format(url, searchType.getKeyWords(), type, i * 100), httpGet));
        }
        httpClient.close();
        return ResultUtil.success(list);
    }

    private List<ArticleModel> sendRequest(CloseableHttpClient httpClient, String uri, HttpGet httpGet) throws IOException, URISyntaxException {
        httpGet.setURI(new URI(uri));
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        List<ArticleModel> articleModels = null;
        if (execute.getStatusLine().getStatusCode() == 200) {
            articleModels = JSON.parseObject(EntityUtils.toString(execute.getEntity())).getJSONObject("data").getJSONArray("rows").toJavaList(ArticleModel.class);
        }
        execute.close();
        return articleModels;
    }
}
