package com.itq.myproject.service.impl;

import com.itq.myproject.service.NumberGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class NumberGenerateServiceImpl implements NumberGenerateService {

    private final Set<String> usedNumbers = new HashSet<>();
    private static final int RANDOM_LENGTH = 5;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public String generateOrderNumber(LocalDate date) {
        String randomPart;
        String orderNumber;

        // Генерация уникального номера
        do {
            randomPart = generateRandomString(RANDOM_LENGTH);
            String datePart = date.format(DATE_FORMATTER);
            orderNumber = randomPart + datePart;
        } while (usedNumbers.contains(orderNumber)); // Проверка на уникальность

        usedNumbers.add(orderNumber);

        return orderNumber;
    }

    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int rand = (int)(Math.random()*9);
            sb.append(rand);
        }
        return sb.toString();
    }
}
