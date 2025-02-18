package com.itq.myproject.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisServiceImpl {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveOrderNumber(String key, String orderNumber) {
        redisTemplate.opsForValue().set(key, orderNumber);
    }

    public String getOrderNumber(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
