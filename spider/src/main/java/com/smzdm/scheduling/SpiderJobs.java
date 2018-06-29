package com.smzdm.scheduling;

import com.smzdm.enums.SpiderConfigEnum;
import com.smzdm.enums.TypeRelationEnum;
import com.smzdm.mapper.ArticleSubscriptionMapper;
import com.smzdm.mapper.BaseEnumMapper;
import com.smzdm.pojo.ArticleSubscription;
import com.smzdm.service.SpiderJobService;
import com.smzdm.service.UpdateCategoryService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Component
public class SpiderJobs {
    @Autowired
    private SpiderJobService spiderJob;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private BaseEnumMapper baseEnumMapper;
    @Value("${custom.turn}")
    private String turn;
    @Value("${custom.category-key}")
    private String categoryKey;
    @Autowired
    private UpdateCategoryService updateCategoryService;
    @Autowired
    private ValueOperations<String, String> valueOperations;
    @Autowired
    private List<ArticleSubscription> articleSubscriptions;

    @Autowired
    private ArticleSubscriptionMapper articleSubscriptionMapper;

    @PostConstruct()
    public void initRedis() {
        TypeRelationEnum[] enums = TypeRelationEnum.values();
        for (TypeRelationEnum value : enums) {
            String[] values = baseEnumMapper.getEnum(value.getKey().split(":")[1]);
            stringRedisTemplate.delete(value.getKey());
            stringRedisTemplate.opsForSet().add(value.getKey(), values);
        }
    }

    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 6 * 1000)
    public void homePageSpider() {
        LocalTime now = LocalTime.now();
        //6点前要没必要那么频繁
        if (now.getHour() < 7) {
            Integer integer = Integer.valueOf(Optional.ofNullable(valueOperations.get(turn)).orElse("0"));
            int remainder = integer % 4;
            valueOperations.set(turn, String.valueOf(remainder + 1));
            if (remainder != 0) {
                return;
            }
        }
        spiderJob.getInfo(SpiderConfigEnum.homeConfig);
    }


    @Scheduled(fixedDelay = 60 * 60 * 1000, initialDelay = 6 * 1000)
    public void homeHistorySpider() {
        LocalTime now = LocalTime.now();
        //6点前要没必要那么频繁
        if (now.getHour() > 7 || now.getHour() < 23) {
            spiderJob.getInfo(SpiderConfigEnum.homeHistoryConfig);
        }
    }

    @Scheduled(fixedDelay = 5 * 60 * 1000, initialDelay = 10 * 1000)
    public void discoverySpider() {
        spiderJob.getInfo(SpiderConfigEnum.latestConfig);
    }

    @Scheduled(fixedDelay = 60 * 60 * 1000, initialDelay = 6 * 1000)
    public void hotSpider() {
        LocalTime now = LocalTime.now();
        if (now.getHour() > 7 || now.getHour() < 23) {
            spiderJob.getInfo(SpiderConfigEnum.hotItemConfig);
        }
    }

    // 秒 分钟 小时 一天
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
    public void updateCategory() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://www.smzdm.com/fenlei/ajax_category_tree/");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        if (execute.getStatusLine().getStatusCode() == 200) {
            String categoryString = EntityUtils.toString(execute.getEntity());
            String categorySha1 = DigestUtils.sha1Hex(categoryString);
            if (!categorySha1.equals(valueOperations.get("category:sha1"))) {
                valueOperations.set("category:sha1", categorySha1);
                updateCategoryService.insert(categoryString);
            }
        }
        httpClient.close();
        execute.close();
    }

    @Scheduled(fixedDelay = 1000 * 60 * 60)
    public void updateSubscription() {
        articleSubscriptions.clear();
        articleSubscriptions.addAll(articleSubscriptionMapper.selectAll());
    }
}