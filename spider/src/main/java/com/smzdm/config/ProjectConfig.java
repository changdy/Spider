package com.smzdm.config;

import com.smzdm.mapper.CategoryMapper;
import com.smzdm.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Autowired
    private CategoryMapper categoryMapper;

    @Bean
    public Category[] categories() {
        return categoryMapper.getCategoryArray();
    }
}
