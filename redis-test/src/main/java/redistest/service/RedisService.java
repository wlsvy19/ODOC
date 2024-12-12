package redistest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    public void saveData(String key, String value) {
        System.out.println("RedisService.saveData");
        redisTemplate.opsForValue().set(key, value);
    }


    public String getData(String key) {
        System.out.println("RedisService.getData");
        return redisTemplate.opsForValue().get(key);
    }


    public void updateData(String key, String value) {
        System.out.println("RedisService.updateData");
        redisTemplate.opsForValue().set(key, value);
    }


    public void deleteData(String key) {
        System.out.println("RedisService.deleteData");
        redisTemplate.delete(key);
    }
}
