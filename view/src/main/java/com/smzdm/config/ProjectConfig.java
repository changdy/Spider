package com.smzdm.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ProjectConfig {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setMaxPayloadLength(5000);
        return loggingFilter;
    }

    @Bean
    public ValueOperations stringValueOperations() {
        return stringRedisTemplate.opsForValue();
    }

    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }


    @Autowired
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @PostConstruct
    public void registerModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(Timestamp.valueOf(value).getTime());
            }
        });
        mappingJackson2HttpMessageConverter.getObjectMapper().registerModule(module);
    }
}
