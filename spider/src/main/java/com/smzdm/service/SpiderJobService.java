package com.smzdm.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smzdm.basemapper.ArticleInfoMapper;
import com.smzdm.basemapper.ArticleJsonMapper;
import com.smzdm.basemapper.ArticleMapper;
import com.smzdm.pojo.Article;
import com.smzdm.pojo.ArticleInfo;
import com.smzdm.pojo.ArticleJson;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
public class SpiderJobService {

    @Resource(name = "infoSpiderConfig")
    private PageProcessor infoSpiderConfig;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private JsonConvertService jsonConvertService;
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private ArticleInfoMapper articleInfoMapper;
    @Resource
    private ArticleJsonMapper articleJsonMapper;

    public void getInfo(String key, String[] url, boolean discovery) {
        Long timeSort = Optional.ofNullable(stringRedisTemplate.opsForValue().get(key)).map(Long::valueOf).orElse(0L);
        Spider spider = Spider.create(infoSpiderConfig);
        List<String> strings = Arrays.asList(url);
        List<ResultItems> resultItems = spider.getAll(strings);
        spider.close();
        JSONArray array = resultItems.stream().map(x -> (JSONArray) x.get("json")).reduce(new JSONArray(), (acc, element) -> {
            acc.addAll(element);
            return acc;
        });
        Stream<JSONObject> stream = array.stream().map(x -> (JSONObject) x).filter(x -> x.containsKey("article_id"));
    }


    private List<Article> generateArticle(Stream<JSONObject> stream, boolean discovery, Long timeSort) {
        Stream<Article> articleStream = stream.filter(x -> x.getLong("timesort") > timeSort).map(jsonConvertService::convertToArticle).filter(Objects::nonNull);
        List<Integer> ids = articleStream.map(Article::getArticleId).collect(toList());
        articleMapper.deleteByIDList(ids);
        List<Article> articles = articleStream.collect(toList());
        articles.forEach(x -> x.setIsDiscovery(discovery));
        return articles;
    }

    private List<ArticleInfo> generateArticleInfo(Stream<JSONObject> stream) {
        LocalDateTime now = LocalDateTime.now();
        List<ArticleInfo> articleInfos = stream.map(jsonConvertService::convertToInfo).filter(Objects::nonNull).collect(toList());
        articleInfos.forEach(x -> x.setUpdateTime(now));
        return articleInfos;
    }

    private List<ArticleJson> generateArticleJson(Stream<JSONObject> stream, boolean discovery, Long timeSort) {
        LocalDateTime now = LocalDateTime.now();
        List<ArticleJson> jsons = stream.filter(x -> x.getLong("timesort") > timeSort).map(x -> {
            ArticleJson articleJson = new ArticleJson();
            articleJson.setContent(x.toJSONString());
            articleJson.setCreateDate(now);
            articleJson.setIsDiscovery(discovery);
            return articleJson;
        }).collect(toList());
        return jsons;
    }
}