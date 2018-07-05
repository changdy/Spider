package com.smzdm.config;

import com.smzdm.mapper.CategoryMapper;
import com.smzdm.pojo.ArticleSubscription;
import com.smzdm.pojo.Category;
import com.smzdm.service.TopicMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class RegisterBean {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private RedisConfig redisConfig;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TopicMessageListener messageListener;

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
    public ValueOperations<String, String> valueOperations(StringRedisTemplate stringRedisTemplate) {
        return stringRedisTemplate.opsForValue();
    }

    @Bean
    public RedisMessageListenerContainer configRedisMessageListenerContainer() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(redisConfig.getCorePoolSize());
        executor.setMaxPoolSize(redisConfig.getMaxPoolSize());
        executor.setQueueCapacity(redisConfig.getQueueCapacity());
        executor.setKeepAliveSeconds(redisConfig.getKeepAliveSeconds());
        executor.setThreadNamePrefix(redisConfig.getThreadNamePrefix());
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 设置Redis的连接工厂
        container.setConnectionFactory(redisTemplate.getConnectionFactory());
        // 设置监听使用的线程池
        container.setTaskExecutor(executor);
        // 设置监听的Topic
        ChannelTopic channelTopic = new ChannelTopic(redisConfig.getExpiredTopic());
        // 设置监听器
        container.addMessageListener(messageListener, channelTopic);
        return container;
    }
}
