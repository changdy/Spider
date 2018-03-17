package com.smzdm.scheduling;

import com.smzdm.enums.SpiderConfigEnum;
import com.smzdm.enums.TypeRelationEnum;
import com.smzdm.mapper.BaseEnumMapper;
import com.smzdm.mapper.CategoryMapper;
import com.smzdm.service.SpiderJobService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Component
public class SpiderJobs {
    @Resource
    private SpiderJobService spiderJob;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private BaseEnumMapper baseEnumMapper;
    @Resource(name = "longValueTemplate")
    private RedisTemplate<String, Long> longValueTemplate;
    @Resource
    private CategoryMapper categoryMapper;
    @Value("${custom.category-key}")
    private String categoryKey;
    @Value("${custom.category_list}")
    private String categoryList;

    @PostConstruct()
    public void initRedis() {
        TypeRelationEnum[] enums = TypeRelationEnum.values();
        for (TypeRelationEnum value : enums) {
            String[] values = baseEnumMapper.getEnum(value.getKey().split(":")[1]);
            stringRedisTemplate.delete(value.getKey());
            stringRedisTemplate.opsForSet().add(value.getKey(), values);
        }
        longValueTemplate.delete(categoryKey);
        Long[] ids = categoryMapper.getIDArray();
        if (ids.length > 0) {
            longValueTemplate.opsForSet().add(categoryKey, ids);
        }
        Map<String, String> map = new HashMap<>();
        categoryMapper.getCategoryMap().forEach(x -> map.put((String) x.get("title"), String.valueOf((Integer) x.get("id"))));
        stringRedisTemplate.opsForHash().putAll(categoryList, map);
    }

    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 6 * 1000)
    public void homePageSpider() {
        spiderJob.getInfo(SpiderConfigEnum.homeConfig);
    }

    //@Scheduled(fixedDelay = 5 * 60 * 1000, initialDelay = 10 * 1000)
    public void discoverySpider() {
        spiderJob.getInfo(SpiderConfigEnum.latestConfig);
    }

}