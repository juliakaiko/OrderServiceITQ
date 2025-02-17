package com.itq.myproject.util;

import com.itq.myproject.model.Order;
import com.itq.myproject.model.OrderDetail;

import java.time.LocalDate;

public class OrderDetailGenerator {
    public static OrderDetail generateOrderDetail() {
        return  OrderDetail.builder()
                .id(1l)
                .quantity(3l)
                .productName("AAAA")
                .productArticle("5555")
                .unitPrice(11l)
                .build();

    }
}
