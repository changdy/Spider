package com.smzdm.scheduling;

import com.smzdm.config.entity.SpiderConfigEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SpiderJobs {

    @Resource
    private SpiderConfigEntity config;


    @Scheduled(fixedDelay = 120 * 1000)
    public void homePageSpider() {

    }

    @Scheduled(cron = "30 0/3 * * * ?")
    public void startDiscoverySpider() {
        //Long timeSort = Optional.ofNullable(stringRedisTemplate.opsForValue().get("timeSort:discovery")).map(Long::valueOf).orElse(0L);
        //Spider spider = Spider.create(infoSpiderConfig);
        //List<ResultItems> all = spider.getAll(new ArrayList<>());
        //spider.close();
    }

}