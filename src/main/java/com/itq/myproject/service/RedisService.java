package com.itq.myproject.service;

public interface RedisService {

    void saveOrderNumber(String key, String orderNumber);

    String getOrderNumber(String key);
}
