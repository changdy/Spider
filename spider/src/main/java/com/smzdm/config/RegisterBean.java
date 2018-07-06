package com.smzdm.config;

import com.smzdm.mapper.CategoryMapper;
import com.smzdm.pojo.ArticleSubscription;
import com.smzdm.pojo.Category;
import com.smzdm.service.TopicMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class RegisterBean {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Bean
    public List<Category> categories() {
        List<Category> categories = Collections.synchronizedList(new ArrayList<Category>());
        categories.addAll(categoryMapper.getCategoryArray());
        return categories;
    }

    @Bean
    public List<ArticleSubscription> articleSubscriptions() {
        return Collections.synchronizedList(new ArrayList<ArticleSubscription>());
    }

    @Bean
    public ValueOperations<String, String> valueOperations() {
        return stringRedisTemplate.opsForValue();
    }
}
