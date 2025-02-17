package com.itq.myproject.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class NumberGenerateServiceImplTest {

    @InjectMocks
    private NumberGenerateServiceImpl numberGenerateService;

    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Test
    public void testGenerateOrderNumber() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 10, 15);
        String expectedDatePart = date.format(DATE_FORMATTER);

        // Act
        String orderNumber = numberGenerateService.generateOrderNumber(date);

        // Assert
        assertNotNull(orderNumber);
        assertTrue(orderNumber.endsWith(expectedDatePart));
        assertEquals(13, orderNumber.length());
    }

    @Test
    public void generateOrderNumber_whenCalledMultipleTimes_thenReturnUniqueNumbers() {
        // Arrange
        LocalDate date = LocalDate.of(2023, 10, 15);
        Set<String> generatedNumbers = new HashSet<>();

        // Act
        for (int i = 0; i < 100; i++) {
            String orderNumber = numberGenerateService.generateOrderNumber(date);
            generatedNumbers.add(orderNumber);
        }

        // Assert
        assertEquals(100, generatedNumbers.size()); // Все номера должны быть уникальными
    }
}