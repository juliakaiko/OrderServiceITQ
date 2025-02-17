package com.itq.myproject.util;

import com.itq.myproject.model.Order;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass //делает класс статическим и запрещает его наследование.
public class OrderGenerator {
    public static Order generateOrder() {
        return Order.builder()
                .id(1l)
                .orderNumber("1111120250220")
                .totalSum(33l)
                .orderDate(LocalDate.of(2025, 02, 10))
                .recipient("Julia")
                .deliveryAddress("Brest")
                .deliveryType("pickup")
                .paymentType("card")
                .build();

    }
}
