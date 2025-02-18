package com.itq.myproject.service.impl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RedisServiceImplTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisServiceImpl redisService;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testSaveOrderNumber() {
        String key = "order:2025-10-01";
        String orderNumber = "1234520251001";

        redisService.saveOrderNumber(key, orderNumber);

        verify(valueOperations, times(1)).set(eq(key), eq(orderNumber));
    }

    @Test
    void testGetOrderNumber() {
        String key = "order:2025-10-01";
        String expectedOrderNumber = "1234520251001";

        when(valueOperations.get(key)).thenReturn(expectedOrderNumber);

        String actualOrderNumber = redisService.getOrderNumber(key);

        assertNotNull(actualOrderNumber);
        assertEquals(expectedOrderNumber, actualOrderNumber);

        verify(valueOperations, times(1)).get(eq(key));
    }

    @Test
    void testGetOrderNumber_NotFound() {
        String key = "order:2025-10-01";

        when(valueOperations.get(key)).thenReturn(null);

        String actualOrderNumber = redisService.getOrderNumber(key);

        assertNull(actualOrderNumber);

        verify(valueOperations, times(1)).get(eq(key));
    }
}
