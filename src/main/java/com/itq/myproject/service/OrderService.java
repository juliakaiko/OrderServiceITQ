package com.itq.myproject.service;

import com.itq.myproject.model.Order;
import com.itq.myproject.model.OrderDetail;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    Order createOrder(Order order);

    Order getOrderById(Long id);

    List<Order> findAllOrders();

    OrderDetail createOrderDetails(OrderDetail orderDetail);

    OrderDetail getOrderDetailById(Long id);

    List<OrderDetail> findAllDetailOrders();

    Order findOrderByOrderDateAndTotalSumGreaterThan(LocalDate date, Long totalSum);

    List<Order> findOrdersExcludingProductAndBetweenDates(LocalDate startDate, LocalDate endDate, String productArticle);

    Order deleteOrderById(Long id);

    Order updateOrder(Order order);

    OrderDetail updateOrderDetails(OrderDetail detail);
}
