package com.smzdm.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smzdm.enums.SpiderConfigEnum;
import com.smzdm.enums.TypeRelationEnum;
import com.smzdm.mapper.ArticleInfoMapper;
import com.smzdm.mapper.ArticleJsonMapper;
import com.smzdm.mapper.ArticleMapper;
import com.smzdm.mapper.BaseEnumMapper;
import com.smzdm.pojo.Article;
import com.smzdm.pojo.ArticleInfo;
import com.smzdm.pojo.ArticleJson;
import com.smzdm.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Service
public class SpiderJobService {

    @Autowired
    private Category[] categories;
    @Autowired
    private PageProcessor jsonProcessor;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private BaseEnumMapper baseEnumMapper;
    @Autowired
    private ArticleInfoMapper articleInfoMapper;
    @Autowired
    private ArticleJsonMapper articleJsonMapper;
    @Autowired
    private JsonConvertService jsonConvertService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${custom.unknown-category}")
    private String unknownCategory;

    private static final Map<String, Short[]> SPECIAL_CATEGORY = new HashMap<>();

    //这几个比较奇葩
    static {
        SPECIAL_CATEGORY.put("食品保健/水饮/咖啡/速溶咖啡/伴侣", new Short[]{95, 101, 5610, 4901});
        SPECIAL_CATEGORY.put("食品保健/生鲜食品/新鲜水果/车厘子/樱桃", new Short[]{95, 111, 853, 5615});
        SPECIAL_CATEGORY.put("食品保健/生鲜食品/新鲜水果/奇异果/猕猴桃", new Short[]{95, 111, 853, 5616});
        SPECIAL_CATEGORY.put("汽车消费/汽车装饰/车用功能用品/挂钩/托盘", new Short[]{147, 159, 551, 2683});
        SPECIAL_CATEGORY.put("汽车消费/汽车装饰/车用功能用品/钥匙挂/包", new Short[]{147, 159, 551, 2681});
    }

    public void getInfo(SpiderConfigEnum spiderConfig) {
        Spider spider = Spider.create(jsonProcessor);
        List<String> strings = Arrays.asList(spiderConfig.getUrl());
        List<ResultItems> resultItems = spider.getAll(strings);
        spider.close();
        JSONArray array = resultItems.stream().map(x -> (JSONArray) x.get("json")).reduce(new JSONArray(), (acc, element) -> {
            acc.addAll(element);
            return acc;
        });
        List<JSONObject> jsonList = array.stream().map(x -> (JSONObject) x).filter(x -> x.containsKey("article_id")).collect(toList());
        generateArticleInfo(jsonList);
        if (spiderConfig.getRedisKey() != null) {
            insertArticle(spiderConfig, jsonList);
        }
    }

    private void generateArticleInfo(List<JSONObject> jsonList) {
        LocalDateTime now = LocalDateTime.now();
        List<ArticleInfo> infoList = jsonList.stream().map(jsonConvertService::convertToInfo).filter(Objects::nonNull).collect(toList());
        infoList.forEach(x -> x.setUpdateTime(now));
        articleInfoMapper.deleteByIDArticleIDs(infoList.stream().map(ArticleInfo::getArticleId).collect(toList()));
        articleInfoMapper.insertHistoryList(infoList);
        articleInfoMapper.insertList(infoList);
    }

    private void insertArticle(SpiderConfigEnum spiderConfig, List<JSONObject> jsonList) {
        String key = spiderConfig.getRedisKey();
        boolean discovery = spiderConfig.isDiscovery();
        long timeSort = Long.valueOf(Optional.ofNullable(stringRedisTemplate.opsForValue().get(key)).orElse("0"));
        generateArticleJson(jsonList, discovery, timeSort);
        stringRedisTemplate.opsForValue().set(key, String.valueOf(generateArticle(jsonList, discovery, timeSort)));
    }

    private Long generateArticle(List<JSONObject> jsonList, boolean discovery, Long timeSort) {
        List<Article> articles = jsonList.stream().filter(x -> x.getLong("timesort") > timeSort).map(jsonConvertService::convertToArticle).collect(toList());
        if (articles.size() > 0) {
            List<Integer> ids = articles.stream().map(Article::getArticleId).collect(toList());
            articleMapper.deleteByIDList(ids);
            for (TypeRelationEnum value : TypeRelationEnum.values()) {
                updateRedisType(articles, value.getKey(), value.getFunction());
            }
            if (discovery) {
                for (Article article : articles) {
                    article.setIsDiscovery(true);
                    article.setCategory(getCategoryID(article.getCategoryStr()));
                }
            } else {
                articles.forEach(x -> x.setIsDiscovery(false));
            }
            articleMapper.insertList(articles);
        }
        return articles.stream().map(Article::getTimeSort).max(Long::compareTo).orElse(timeSort);
    }

    private void generateArticleJson(List<JSONObject> jsonList, boolean discovery, Long timeSort) {
        LocalDateTime now = LocalDateTime.now();
        List<ArticleJson> list = jsonList.stream().filter(x -> x.getLong("timesort") > timeSort).map(x -> {
            ArticleJson articleJson = new ArticleJson();
            articleJson.setContent(x.toJSONString());
            articleJson.setCreateDate(now);
            articleJson.setIsDiscovery(discovery);
            return articleJson;
        }).collect(toList());
        if (list.size() > 0) {
            articleJsonMapper.insertList(list);
        }
    }

    private void updateRedisType(List<Article> articles, String key, Function<Article, String> function) {
        Set<String> redisMembers = stringRedisTemplate.opsForSet().members(key);
        List<String> streamMembers = articles.stream().map(function).filter(Objects::nonNull).distinct().collect(toList());
        streamMembers.removeAll(redisMembers);
        if (streamMembers.size() > 0) {
            stringRedisTemplate.opsForSet().add(key, streamMembers.toArray(new String[streamMembers.size()]));
            streamMembers.forEach(x -> baseEnumMapper.addEnum(key.split(":")[1], "'" + x.replaceAll("'", "''") + "'"));
        }
    }

    private Short[] getCategoryID(String categoryStr) {
        if (categoryStr != null) {
            if (categoryStr.indexOf("/") == 0) {
                categoryStr = categoryStr.substring(1);
            }
            if (categoryStr.length() > 0) {
                String[] split = categoryStr.split("/");
                if (split.length == 5) {
                    if (SPECIAL_CATEGORY.containsKey(categoryStr)) {
                        SPECIAL_CATEGORY.get(categoryStr);
                    }
                }
                if (!split[0].equals("无")) {
                    int length = split.length;
                    int count = 0;
                    for (String s : split) {
                        if (null == s || s.equals("无")) {
                            count++;
                        }
                    }
                    Short[] arr = new Short[length - count];
                    for (int i = 0; i < length - count; i++) {
                        arr[i] = checkInArr(split[i]);
                    }
                    return arr;
                }
            }
        }
        stringRedisTemplate.opsForList().rightPush(unknownCategory, categoryStr);
        return null;
    }

    private Short checkInArr(String title) {
        for (Category category : categories) {
            if (category.getTitle().equals(title)) {
                return category.getId();
            }
        }
        return null;
    }
}