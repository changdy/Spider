package com.smzdm.scheduling;

import com.smzdm.Enum.TypeRelationEnum;
import com.smzdm.mapper.EnumMapper;
import com.smzdm.service.SpiderJobService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


@Component
public class SpiderJobs {


    @Resource
    private SpiderJobService spiderJob;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private EnumMapper enumMapper;

    @PostConstruct()
    public void initRedis() {
        TypeRelationEnum[] enums = TypeRelationEnum.values();
        for (TypeRelationEnum value : enums) {
            String[] values = enumMapper.getEnum(value.getKey().split(":")[1]);
            stringRedisTemplate.delete(value.getKey());
            stringRedisTemplate.opsForSet().add(value.getKey(), values);
        }
    }
}