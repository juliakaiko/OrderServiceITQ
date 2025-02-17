package com.itq.myproject.service.impl;

import com.itq.myproject.model.Order;
import com.itq.myproject.model.OrderDetail;
import com.itq.myproject.repository.OrderDetailRepository;
import com.itq.myproject.repository.OrderRepository;
import com.itq.myproject.util.OrderDetailGenerator;
import com.itq.myproject.util.OrderGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class) // Инициализирует моки
public class OrderServiceImplTest {

    @InjectMocks //создает имитирующую реализацию аннотированного типа и внедряет в нее зависимые имитирующие объекты
    private OrderServiceImpl service;
    @Mock // создает фиктивную реализацию для класса
    private OrderRepository orderRepository;
    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Test
    public void createOrder_whenCorrect_thenReturnOrder() {
        Order order = OrderGenerator.generateOrder();
        when(orderRepository.save(order)).thenReturn(order);

        Order result = service.createOrder(order);

        assertNotNull(result);
        assertEquals(order, result);

        verify(orderRepository, times(1)).save(order);
        log.info("Order created: {}", result);
    }

    @Test
    public void getOrderById_whenCorrect_thenReturnOrder() {
        Order order = OrderGenerator.generateOrder();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = service.getOrderById(1L);

        assertNotNull(result);
        assertEquals(order, result);
        verify(orderRepository, times(1)).findById(1L);
        log.info("Order found: {}", result);
    }

    @Test
    public void getOrderDetailById_whenCorrect_thenReturnOrderDetail() {
        OrderDetail orderDetail = OrderDetailGenerator.generateOrderDetail();
        when(orderDetailRepository.findById(1L)).thenReturn(Optional.of(orderDetail));

        OrderDetail result = service.getOrderDetailById(1L);

        assertNotNull(result);
        assertEquals(orderDetail, result);

        verify(orderDetailRepository, times(1)).findById(1L);
        log.info("OrderDetail found: {}", result);
    }

    @Test
    public void findAllOrders_whenCorrect_thenReturnOrderList() {
        Order order = OrderGenerator.generateOrder();
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);orderList.add(order);

        when(orderRepository.findAll()).thenReturn(orderList);

        List<Order> result = service.findAllOrders();

        assertFalse(result.isEmpty());
        assertEquals(orderList, result);

        verify(orderRepository, times(1)).findAll();
        log.info("Orders found: {}", result);
    }

    @Test
    public void findAllDetailOrders_whenCorrect_thenReturnOrderDetailsList() {
        OrderDetail orderDetail = OrderDetailGenerator.generateOrderDetail();
        List<OrderDetail> orderDetailList = new ArrayList<>();
        orderDetailList.add(orderDetail);orderDetailList.add(orderDetail);

        when(orderDetailRepository.findAll()).thenReturn(orderDetailList);

        List<OrderDetail> result = service.findAllDetailOrders();

        assertFalse(result.isEmpty());
        assertEquals(result, orderDetailList);

        verify(orderDetailRepository, times(1)).findAll();
        log.info("OrderDetails found: {}", result);
    }

    @Test
    public void createOrderDetails_whenCorrect_thenReturnOrderDetails() {
        OrderDetail orderDetail = OrderDetailGenerator.generateOrderDetail();
        when(orderDetailRepository.save(orderDetail)).thenReturn(orderDetail);

        OrderDetail result = service.createOrderDetails(orderDetail);

        assertNotNull(result);
        assertEquals(result, orderDetail);

        verify(orderDetailRepository, times(1)).save(orderDetail);
        log.info("OrderDetail created: {}", result);
    }

    @Test
    public void findOrderByOrderDateAndTotalSumGreaterThan_whenCorrect_thenReturnOrder() {
        Order order = OrderGenerator.generateOrder();
        LocalDate orderDate = LocalDate.of(2025, 2, 10);
        Long totalSum =10L;
        when(orderRepository.findOrderByOrderDateAndTotalSumGreaterThan(orderDate, totalSum))
                .thenReturn(Optional.of(order));

        Order result = service.findOrderByOrderDateAndTotalSumGreaterThan(orderDate, totalSum);

        assertNotNull(result);

        verify(orderRepository, times(1)).findOrderByOrderDateAndTotalSumGreaterThan(any(LocalDate.class), anyLong());
        log.info("Order found by date and sum: {}", result);
    }

    @Test
    public void findOrdersExcludingProductAndBetweenDates_whenCorrect_thenReturnOrder() {
        Order order = OrderGenerator.generateOrder();
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);

        LocalDate startDate = LocalDate.of(2025, 02, 10);
        LocalDate endDate = LocalDate.now();
        String productArticle = "5555";
        when(orderRepository.findOrdersExcludingProductAndBetweenDates(startDate, endDate, productArticle)).thenReturn(orderList);

        List<Order> result = service.findOrdersExcludingProductAndBetweenDates(startDate, endDate, productArticle);

        assertFalse(result.isEmpty());
        verify(orderRepository, times(1)).findOrdersExcludingProductAndBetweenDates(any(LocalDate.class), any(LocalDate.class), anyString());
        log.info("Orders found excluding product and between dates: {}", result);
    }

    @Test
    public void deleteOrderById_whenCorrect_thenReturnOrder() {
        Order order = OrderGenerator.generateOrder();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        doNothing().when(orderRepository).deleteById(1L);

        Order result = service.deleteOrderById(1L);

        assertNotNull(result);
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).deleteById(1L);
        log.info("Order deleted: {}", result);
    }

    @Test
    public void updateOrder_whenCorrect_thenReturnOrder() {
        Order order = OrderGenerator.generateOrder();
        order.setRecipient("Mike");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order result = service.updateOrder(order);

        assertNotNull(result);

        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(order);
        log.info("Order updated: {}", result);
    }

    @Test
    public void updateOrderDetails_whenCorrect_thenReturnOrderDetails() {
        OrderDetail orderDetail = OrderDetailGenerator.generateOrderDetail();
        orderDetail.setUnitPrice(1000L);
        when(orderDetailRepository.findById(1L)).thenReturn(Optional.of(orderDetail));
        when(orderDetailRepository.save(orderDetail)).thenReturn(orderDetail);

        OrderDetail result = service.updateOrderDetails(orderDetail);

        assertNotNull(result);

        verify(orderDetailRepository, times(1)).findById(1L);
        verify(orderDetailRepository, times(1)).save(orderDetail);
        log.info("OrderDetail updated: {}", result);
    }
}