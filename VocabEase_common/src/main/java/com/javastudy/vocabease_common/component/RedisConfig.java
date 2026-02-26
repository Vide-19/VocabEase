package com.javastudy.vocabease_common.component;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration("redisConfig")
public class RedisConfig<V> {

    @Bean
    public RedisTemplate<String, V> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, V> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        //key序列化
        template.setKeySerializer(RedisSerializer.string());
        //value序列化
        template.setValueSerializer(RedisSerializer.json());
        //hash key序列化
        template.setHashKeySerializer(RedisSerializer.string());
        //hash value序列化
        template.setHashValueSerializer(RedisSerializer.json());
        template.afterPropertiesSet();
        return template;
    }

}
























