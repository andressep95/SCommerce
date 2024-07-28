package cl.playground.scommerce.config;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisConnection {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisConnection(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void clearCache(String key) {
        redisTemplate.delete(key);
    }
}