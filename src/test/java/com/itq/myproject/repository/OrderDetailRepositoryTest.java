package com.itq.myproject.repository;

import com.itq.myproject.model.Order;
import com.itq.myproject.model.OrderDetail;
import com.itq.myproject.util.OrderDetailGenerator;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest // астраивает тестовую базу данных и управляет транзакциями.
public class OrderDetailRepositoryTest {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    private static OrderDetail expectedOrderDetail;

    @BeforeClass
    public static void setUp(){
        expectedOrderDetail = OrderDetailGenerator.generateOrderDetail();
    }

    @Test
    public void testFindOrderDetailById() {
        this.orderDetailRepository.save(expectedOrderDetail);
        OrderDetail actualOrderDetail = orderDetailRepository.findById(1L).get();
        log.info("Test to find the OrderDetail with id = 1: "+actualOrderDetail);

        Assert.assertNotNull(actualOrderDetail);
        Assert.assertEquals(expectedOrderDetail, actualOrderDetail);
    }

    @Test
    public void testSaveOrderDetail() {
        this.orderDetailRepository.save(expectedOrderDetail);
        OrderDetail actualOrderDetail =  orderDetailRepository.findById(expectedOrderDetail.getId()).get();
        log.info("Test to save the OrderDetail: "+actualOrderDetail);

        Assertions.assertEquals(expectedOrderDetail,actualOrderDetail);
    }

    @Test
    public void testDeleteOrderDetail() {
        this.orderDetailRepository.save(expectedOrderDetail);
        OrderDetail actualOrderDetail =  orderDetailRepository.findById(expectedOrderDetail.getId()).get();
        orderDetailRepository.delete(actualOrderDetail);
        log.info("Test to delete the Order with id = 1: "+actualOrderDetail);
        Optional<OrderDetail> deletedOrderDetail = orderDetailRepository.findById(actualOrderDetail.getId());

        Assertions.assertFalse(deletedOrderDetail.isPresent());
    }

    @Test
    public void testFindAllOrders() {
        this.orderDetailRepository.save(expectedOrderDetail);
        log.info("Test to find all theOrders : "+ this.orderDetailRepository.findAll());

        Assertions.assertFalse(this.orderDetailRepository.findAll().isEmpty(),() -> "List of orders shouldn't be empty");
    }
}