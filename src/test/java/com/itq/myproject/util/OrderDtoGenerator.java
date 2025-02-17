package com.itq.myproject.util;

import com.itq.myproject.dto.OrderDto;

import java.time.LocalDate;

public class OrderDtoGenerator {
    public static OrderDto generateOrderDto() {
        return  OrderDto.builder()
                .id(1l)
                .quantity(1l)
                .productName("AAAA")
                .productArticle("5555")
                .unitPrice(11l)
                .orderNumber("1111120250220")
                .totalSum(33l)
                .orderDate(LocalDate.of(2025, 2, 10))
                .recipient("Julia")
                .deliveryAddress("Brest")
                .deliveryType("pickup")
                .paymentType("card")
                .build();
    }
}
