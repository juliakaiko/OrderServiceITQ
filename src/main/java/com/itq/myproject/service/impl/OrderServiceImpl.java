package com.itq.myproject.service.impl;

import com.itq.myproject.exception.NotFoundException;
import com.itq.myproject.model.Order;
import com.itq.myproject.model.OrderDetail;
import com.itq.myproject.repository.OrderDetailRepository;
import com.itq.myproject.repository.OrderRepository;
import com.itq.myproject.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Transactional
    public Order createOrder(Order order) {
        log.info("Order was created: ",order);
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        Optional <Order> order = orderRepository.findById(id);
        log.info("Order was found: ", order.get());
        return order.orElseThrow(() -> new NotFoundException("Order not found"));
    }

    @Transactional(readOnly = true)
    public OrderDetail getOrderDetailById(Long id) {
        Optional <OrderDetail> detail =orderDetailRepository.findById(id);
        log.info("OrderDetail was found: ", detail.get());
        return detail.orElseThrow(() -> new NotFoundException("OrderDetail  not found"));
    }

    @Transactional(readOnly = true)
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<OrderDetail> findAllDetailOrders() {
        return orderDetailRepository.findAll();
    }

    @Transactional
    public OrderDetail createOrderDetails(OrderDetail orderDetail) {
        log.info("OrderDetail was created: ",orderDetail);
        return orderDetailRepository.save(orderDetail);
    }

    @Transactional(readOnly = true)
    public Order findOrderByOrderDateAndTotalSumGreaterThan(LocalDate date, Long totalSum) {
        log.info("Order was found by date = "+ date + " and sum greater than totalSum = "+totalSum);
        Optional<Order> order = Optional.ofNullable(orderRepository.findOrderByOrderDateAndTotalSumGreaterThan(date, totalSum)
                .orElseThrow(() -> new NotFoundException("Order wasn't found findOrderByOrderDateAndTotalSumGreaterThan {} ")));
        return order.get();
    }

    @Transactional(readOnly = true)
    public List<Order> findOrdersExcludingProductAndBetweenDates(LocalDate startDate, LocalDate endDate, String productArticle) {
        log.info("Orders were found between dates = "+ startDate+ " and "
                + "and excluding this product with productArticle= "+productArticle);
        return orderRepository.findOrdersExcludingProductAndBetweenDates(startDate, endDate, productArticle);
    }

    @Transactional
    public Order deleteOrderById(Long id) {
        Order deletedOrder = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
        orderRepository.deleteById(id);
        log.info("Order was deleted: ", deletedOrder);
        return deletedOrder;
    }

    @Transactional
    public Order updateOrder(Order order) {
        // Проверяем, существует ли запись с таким ID
        Order existingOrder = orderRepository.findById(order.getId())
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + order.getId()));

        // Обновляем поля существующей записи
        existingOrder.setOrderNumber(order.getOrderNumber());
        existingOrder.setTotalSum(order.getTotalSum());
        existingOrder.setOrderDate(order.getOrderDate());
        existingOrder.setRecipient(order.getRecipient());
        existingOrder.setDeliveryAddress(order.getDeliveryAddress());
        existingOrder.setPaymentType(order.getPaymentType());
        existingOrder.setDeliveryType(order.getDeliveryType());

        // Сохраняем обновленную запись
        Order savedOrder = orderRepository.save(existingOrder);
        log.info("Order was updated: {}", savedOrder);
        return savedOrder;
    }

    @Transactional
    public OrderDetail updateOrderDetails(OrderDetail detail) {
        // Проверяем, существует ли запись с таким ID
        OrderDetail existingOrderDetail = orderDetailRepository.findById(detail.getId())
                .orElseThrow(() -> new NotFoundException("OrderDetail not found with id: " + detail.getId()));

        // Обновляем поля существующей записи
        existingOrderDetail.setQuantity(detail.getQuantity());
        existingOrderDetail.setUnitPrice(detail.getUnitPrice());
        existingOrderDetail.setProductName(detail.getProductName());
        existingOrderDetail.setProductArticle(detail.getProductArticle());

        // Сохраняем обновленную запись
        OrderDetail savedOrderDetail = orderDetailRepository.save(existingOrderDetail);
        log.info("OrderDetail was updated: {}", savedOrderDetail);
        return savedOrderDetail;
    }
}
