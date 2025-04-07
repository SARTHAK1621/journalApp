package net.engineeringdigest.journalApp.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;
    @Disabled
    @Test
    public void test(){
        redisTemplate.opsForValue().set("email","sarthak@gmail.com");
        String email = redisTemplate.opsForValue().get("email").toString();
        String name=redisTemplate.opsForValue().get("name").toString();
        System.out.println(email);
        System.out.println(name);
    }
}
