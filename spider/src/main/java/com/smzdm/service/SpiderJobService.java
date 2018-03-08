package com.smzdm.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smzdm.Enum.SpiderConfigEnum;
import com.smzdm.basemapper.ArticleInfoMapper;
import com.smzdm.basemapper.ArticleJsonMapper;
import com.smzdm.basemapper.ArticleMapper;
import com.smzdm.pojo.Article;
import com.smzdm.pojo.ArticleInfo;
import com.smzdm.pojo.ArticleJson;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
public class SpiderJobService {

    @Resource(name = "infoSpiderConfig")
    private PageProcessor infoSpiderConfig;
    @Resource
    private JsonConvertService jsonConvertService;
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private ArticleInfoMapper articleInfoMapper;
    @Resource
    private ArticleJsonMapper articleJsonMapper;
    @Resource
    private RedisTemplate<String, Long> longValueTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void getInfo(SpiderConfigEnum spiderConfig) {
        Spider spider = Spider.create(infoSpiderConfig);
        List<String> strings = Arrays.asList(spiderConfig.getUrl());
        List<ResultItems> resultItems = spider.getAll(strings);
        spider.close();
        JSONArray array = resultItems.stream().map(x -> (JSONArray) x.get("json")).reduce(new JSONArray(), (acc, element) -> {
            acc.addAll(element);
            return acc;
        });
        Stream<JSONObject> stream = array.stream().map(x -> (JSONObject) x).filter(x -> x.containsKey("article_id"));
        generateArticleInfo(stream);
        if (spiderConfig.getRedisKey() != null) {
            insertArticle(spiderConfig, stream);
        }
    }

    //todo 各种插入数据库
    private void generateArticleInfo(Stream<JSONObject> stream) {
        LocalDateTime now = LocalDateTime.now();
        List<ArticleInfo> infoList = stream.map(jsonConvertService::convertToInfo).filter(Objects::nonNull).collect(toList());
        infoList.forEach(x -> x.setUpdateTime(now));
    }

    private void insertArticle(SpiderConfigEnum spiderConfig, Stream<JSONObject> stream) {
        String key = spiderConfig.getRedisKey();
        boolean discovery = spiderConfig.isDiscovery();
        long timeSort = Optional.ofNullable(longValueTemplate.opsForValue().get(key)).orElse(0L);
        timeSort = generateArticle(stream, discovery, timeSort);
        generateArticleJson(stream, discovery, timeSort);
        longValueTemplate.opsForValue().set(key, timeSort);
    }

    private Long generateArticle(Stream<JSONObject> stream, boolean discovery, Long timeSort) {
        Stream<Article> articleStream = stream.filter(x -> x.getLong("timesort") > timeSort).map(jsonConvertService::convertToArticle).filter(Objects::nonNull);
        List<Integer> ids = articleStream.map(Article::getArticleId).collect(toList());
        articleMapper.deleteByIDList(ids);
        List<Article> articles = articleStream.collect(toList());
        articles.forEach(x -> x.setIsDiscovery(discovery));
        updateRedisType(articleStream, "article:mall", Article::getMall);
        updateRedisType(articleStream, "article:channel", Article::getChannel);
        updateRedisType(articleStream, "article:type", Article::getType);
        updateRedisType(articleStream, "article:yh_type", Article::getYhType);
        return articleStream.map(Article::getTimeSort).max(Long::compareTo).orElse(timeSort);
    }

    private void generateArticleJson(Stream<JSONObject> stream, boolean discovery, Long timeSort) {
        LocalDateTime now = LocalDateTime.now();
        List<ArticleJson> jsonList = stream.filter(x -> x.getLong("timesort") > timeSort).map(x -> {
            ArticleJson articleJson = new ArticleJson();
            articleJson.setContent(x.toJSONString());
            articleJson.setCreateDate(now);
            articleJson.setIsDiscovery(discovery);
            return articleJson;
        }).collect(toList());
    }

    private void updateRedisType(Stream<Article> articleStream, String key, Function<Article, String> function) {
        Set<String> redisMembers = stringRedisTemplate.opsForSet().members(key);
        List<String> streamMembers = articleStream.map(function).distinct().collect(toList());
        streamMembers.removeAll(redisMembers);
        if (streamMembers.size() > 0) {
            stringRedisTemplate.opsForSet().add("key", (String[]) streamMembers.toArray());
        }
    }
}