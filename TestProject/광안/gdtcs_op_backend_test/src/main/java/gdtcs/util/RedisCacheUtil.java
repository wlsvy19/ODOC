package gdtcs.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
public class RedisCacheUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisCacheUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Redis 키 생성 (key prefix + 파라미터 정렬 조합)
    public String generateKey(String prefix, Map<String, Object> param) {
        StringBuilder keyBuilder = new StringBuilder(prefix).append("::");
        param.keySet().stream()
                .sorted()
                .forEach(k -> {
                    Object value = param.get(k);
                    keyBuilder.append(k).append("=").append(value != null ? value.toString() : "").append("&");
                });
        if (keyBuilder.charAt(keyBuilder.length() - 1) == '&') {
            keyBuilder.setLength(keyBuilder.length() - 1);
        }
        return keyBuilder.toString();
    }

    public <T> T getOrSet(
            String prefix,
            Map<String, Object> param,
            long ttl,
            TimeUnit unit,
            Supplier<T> dbSupplier) {

        String redisKey = generateKey(prefix, param);
        @SuppressWarnings("unchecked")
        T cachedData = (T) redisTemplate.opsForValue().get(redisKey);
        if (cachedData != null) {
            System.out.println("Returning data from Redis cache for key: " + redisKey);
            return cachedData;
        }

        // 없으면 DB 조회 및 캐싱
        T result = dbSupplier.get();
        redisTemplate.opsForValue().set(redisKey, result, ttl, unit);
        return result;
    }

}
