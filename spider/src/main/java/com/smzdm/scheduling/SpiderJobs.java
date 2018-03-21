package com.smzdm.scheduling;

import com.smzdm.enums.SpiderConfigEnum;
import com.smzdm.enums.TypeRelationEnum;
import com.smzdm.mapper.BaseEnumMapper;
import com.smzdm.service.SpiderJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
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
            Integer integer = Integer.valueOf(Optional.ofNullable(stringRedisTemplate.opsForValue().get(turn)).orElse("0"));
            int remainder = integer % 4;
            stringRedisTemplate.opsForValue().set(turn, String.valueOf(remainder + 1));
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

}