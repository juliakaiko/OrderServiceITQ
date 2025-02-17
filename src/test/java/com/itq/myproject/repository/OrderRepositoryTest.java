package com.itq.myproject.repository;

import com.itq.myproject.model.Order;
import com.itq.myproject.util.OrderGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest // астраивает тестовую базу данных и управляет транзакциями.
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    private static Order expectedOrder;

    @BeforeClass
    public static void setUp(){
        expectedOrder = OrderGenerator.generateOrder();
    }

    @Test
    public void testFindOrderById() {
        this.orderRepository.save(expectedOrder);
        Order actualOrder = orderRepository.findById(1L).get();
        log.info("Test to find the Order with id = 1: "+actualOrder);

        Assert.assertNotNull(actualOrder);
        Assert.assertEquals(expectedOrder, actualOrder);
    }

    @Test
    public void testSaveOrder() {
        this.orderRepository.save(expectedOrder);
        Order actualOrder =  orderRepository.findById(expectedOrder.getId()).get();
        log.info("Test to save the Order: "+actualOrder);

        Assertions.assertEquals(expectedOrder,actualOrder);
    }

    @Test
    public void testDeleteOrder() {
        this.orderRepository.save(expectedOrder);
        Order actualOrder =  orderRepository.findById(expectedOrder.getId()).get();
        orderRepository.delete(actualOrder);
        log.info("Test to delete the Order with id = 1: "+actualOrder);
        Optional<Order> deletedOrder = orderRepository.findById(actualOrder.getId());

        Assertions.assertFalse(deletedOrder.isPresent());
    }

    @Test
    public void testFindAllOrders() {
        this.orderRepository.save(expectedOrder);
        log.info("Test to find all theOrders : "+ this.orderRepository.findAll());

        Assertions.assertFalse(this.orderRepository.findAll().isEmpty(),() -> "List of orders shouldn't be empty");
    }

    @Test
    public void testFindOrderByOrderDateAndTotalSumGreaterThan() {
        this.orderRepository.save(expectedOrder);
        LocalDate orderDate = LocalDate.of(2025, 2, 10);
        Long totalSum =10L;

        Optional<Order> optionalOrder = orderRepository.findOrderByOrderDateAndTotalSumGreaterThan(orderDate, totalSum);

        log.info("Test to find the order by OrderDate " + orderDate +
                " and TotalSum greater than " + totalSum + ". The Order was found = " + optionalOrder.isPresent());

        // Проверяем, что заказ найден
        Assert.assertTrue( "The order wasn't found ",optionalOrder.isPresent());
        Order actualOrder = optionalOrder.get();

        Assert.assertNotNull(actualOrder);
        Assert.assertEquals(expectedOrder, actualOrder);
    }

    @Test
    public void testFindOrdersExcludingProductAndBetweenDates() {
        this.orderRepository.save(expectedOrder);
        LocalDate startDate = LocalDate.of(2025, 02, 10);
        LocalDate endDate = LocalDate.now();
        String productArticle = "5555";

        List<Order> actualOrderList = orderRepository.findOrdersExcludingProductAndBetweenDates(startDate, endDate, productArticle);

        log.info("Test to find the order between dates " +startDate+
                "and excluding product with article "+productArticle+ ". The number of orders was found = " + actualOrderList);

        Assertions.assertFalse(actualOrderList.isEmpty(),() -> "List of orders shouldn't be empty");
    }

}