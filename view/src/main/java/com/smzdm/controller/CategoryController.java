package com.smzdm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Changdy on 2018/4/11.
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${custom.category-key}")
    private String categoryKey;

}