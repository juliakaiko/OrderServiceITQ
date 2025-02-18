package com.itq.myproject.controller;

import com.itq.myproject.dto.FilterBySumDto;
import com.itq.myproject.dto.FilterExcludingProductDto;
import com.itq.myproject.dto.OrderDto;
import com.itq.myproject.model.*;
import com.itq.myproject.service.*;
import com.itq.myproject.util.OrderDetailGenerator;
import com.itq.myproject.util.OrderDtoGenerator;
import com.itq.myproject.util.OrderGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
@ExtendWith(MockitoExtension.class)
@Slf4j // для логирования
class OrderControllerTest {

    private final static Long ENTITY_ID = 1l;

    @MockBean  //создает мок-объект и добавляет его в контекст Spring как bin
    private OrderService orderService;

    @MockBean
    private NumberGenerateService numberGenerateService;

    @MockBean
    private BindingResult bindingResult;

    @InjectMocks //создание экземпляра класса и внедрение в него макетов, созданных с помощью аннотации @Mock
    private OrderController orderController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void showOrderList_whenCorrect_thenOk() throws Exception {
        List<Order> orderList = new ArrayList<>();
        Order order = OrderGenerator.generateOrder();
        orderList.add(order);
        orderList.add(order);

        when(orderService.findAllOrders()).thenReturn(orderList);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("order-list"))
                .andExpect(model().attributeExists("orders"));

        verify(orderService, times(1)).findAllOrders();
    }

   @Test
    void getOrderById_whenCorrect_thenOk() throws Exception {
        Order order = OrderGenerator.generateOrder();
        when(orderService.getOrderById(ENTITY_ID)).thenReturn(order);

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(model().attributeExists("orderDto"));

        verify(orderService, times(1)).getOrderById(1L);
    }


    @Test
    void getOrderDetailById_whenCorrect_thenOk() throws Exception {
        OrderDetail response = OrderDetailGenerator.generateOrderDetail();
        when(orderService.getOrderDetailById(ENTITY_ID)).thenReturn(response);

        mockMvc.perform(get("/orders/detail/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("detail"))
                .andExpect(model().attributeExists("detail"));

        verify(orderService, times(1)).getOrderDetailById(1L);
    }

    @Test
    void showOrderDetailList_whenCorrect_thenOk() throws Exception {
        List<OrderDetail> detailList = new ArrayList<>();
        OrderDetail detail = OrderDetailGenerator.generateOrderDetail();
        detailList.add(detail);
        when(orderService.findAllDetailOrders()).thenReturn(detailList);

        mockMvc.perform(get("/orders/details"))
                .andExpect(status().isOk())
                .andExpect(view().name("detail-list"))
                .andExpect(model().attributeExists("details"));

        verify(orderService, times(1)).findAllDetailOrders();
    }

    @Test
    void showAddForm_whenCorrect_thenOk() throws Exception {
        mockMvc.perform(get("/orders/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add_order"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    void createOrder_whenCorrect_thenRedirect() throws Exception {
        // Arrange - настройка
        OrderDto orderDto = OrderDtoGenerator.generateOrderDto();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(numberGenerateService.generateOrderNumber(LocalDate.of(2025, 2, 10)))
                .thenReturn("1111120250210");

        // Act & Assert - выполнение сценария и проверка
        mockMvc.perform(post("/orders/add")
                        .flashAttr("order", orderDto)) //добавления в запрос flash-атрибутов, которые которые сохраняются на время перенаправления (redirect) и затем автоматически удаляются после того, как запрос будет обработан.
                .andExpect(status().is3xxRedirection())// проверка, что статус ответа соответствует перенаправлению (код 3xx).
                .andExpect(view().name("redirect:/orders"));

        // Verify - проверить, сколько раз выполнился метод
        verify(numberGenerateService, times(1)).generateOrderNumber(orderDto.getOrderDate());
        verify(orderService, times(1)).createOrder(any(Order.class));
        verify(orderService, times(1)).createOrderDetails(any(OrderDetail.class));
    }

    @Test
    void createOrder_whenIncorrect_thenAddDataAgain()  throws Exception {
        OrderDto orderDto = new OrderDto();

        when(bindingResult.hasErrors()).thenReturn(true);

        mockMvc.perform(post("/orders/add")
                    .flashAttr("order", orderDto))
            .andExpect(status().isOk())
            .andExpect(view().name("add_order"))
            .andExpect(model().attributeHasErrors("order"));

        verify(orderService, never()).createOrder(any(Order.class));
        verify(orderService, never()).createOrderDetails(any(OrderDetail.class));
    }

    @Test
    void showFilterFormOne_whenCorrect_thenOk() throws Exception {
        mockMvc.perform(get("/orders/filter_by_date_and_sum"))
                .andExpect(status().isOk())
                .andExpect(view().name("filter_form_1"))
                .andExpect(model().attributeExists("orderDto"));
    }

    @Test
    void getOrderByOrderDateAndTotalSumGreaterThan_ValidInput()  throws Exception {
        Order order = OrderGenerator.generateOrder();

        when(orderService.findOrderByOrderDateAndTotalSumGreaterThan(LocalDate.of(2025, 2, 10), 10L))
                .thenReturn(order);

        mockMvc.perform(post("/orders/filter_by_date_and_sum")
                        .param("orderDate", "2025-02-10")
                        .param("totalSum", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(model().attributeExists("orderDto"));

        verify(orderService, times(1))
                .findOrderByOrderDateAndTotalSumGreaterThan(any(LocalDate.class), anyLong());
    }

    @Test
    void getOrderByOrderDateAndTotalSumGreaterThan_InValidInput()  throws Exception {
        // Arrange
        FilterBySumDto orderDto = new FilterBySumDto();
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/orders/filter_by_date_and_sum")
                        .flashAttr("orderDto", orderDto)
                        .flashAttr("org.springframework.validation.BindingResult.orderDto", bindingResult))
                .andExpect(status().isOk())
                .andExpect(view().name("filter_form_1"))
                .andExpect(model().attributeExists("errors"))
                .andExpect(model().attributeExists("orderDto"));

        // Verify
        verify(orderService, never()).findOrderByOrderDateAndTotalSumGreaterThan(any(), any());
    }

    @Test
    void showFilterFormTwo_whenCorrect_thenOk()  throws Exception {
        mockMvc.perform(get("/orders/filter_between_dates_and_excluding_product"))
                .andExpect(status().isOk())
                .andExpect(view().name("filter_form_2"))
                .andExpect(model().attributeExists("orderDto"));
    }

    @Test
    void findOrdersBetweenStartDateAndEndDateANDExcludingProduct_ValidInput() throws Exception {
        Order order = OrderGenerator.generateOrder();
        List <Order> orderList = new LinkedList<>();
        orderList.add(order);
        when(orderService.findOrdersExcludingProductAndBetweenDates(LocalDate.of(2025, 2, 01),
                LocalDate.of(2025, 2, 10), "12345"))
                .thenReturn(orderList);

        mockMvc.perform(post("/orders/filter_between_dates_and_excluding_product")
                        .param("startDate", "2025-02-01")
                        .param("endDate", "2025-02-10")
                        .param("productArticle", "12345"))
                .andExpect(status().isOk())
                .andExpect(view().name("order-list"))
                .andExpect(model().attributeExists("orders"));

        verify(orderService, times(1))
                .findOrdersExcludingProductAndBetweenDates(any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    void findOrdersBetweenStartDateAndEndDateANDExcludingProduct_InvalidInput() throws Exception {
        FilterExcludingProductDto filterDto = new FilterExcludingProductDto();
        when(bindingResult.hasErrors()).thenReturn(true);

        mockMvc.perform(post("/orders/filter_between_dates_and_excluding_product")
                        .flashAttr("filterDto", filterDto)
                        .flashAttr("org.springframework.validation.BindingResult.orderDto", bindingResult))
                .andExpect(status().isOk())
                .andExpect(view().name("filter_form_2"))
                .andExpect(model().attributeExists("errors"))
                .andExpect(model().attributeExists("orderDto"));;

        verify(orderService, never())
                .findOrdersExcludingProductAndBetweenDates(any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    void deleteOrderById_whenCorrect_thenRedirect()  throws Exception {
        mockMvc.perform(delete("/orders/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/orders"));

        verify(orderService, times(1)).deleteOrderById(1L);
    }

    @Test
    void showUpdateForm_whenCorrect_thenOk()  throws Exception {
        Order order = OrderGenerator.generateOrder();
        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/orders/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("update_order"))
                .andExpect(model().attributeExists("orderDto"));

        verify(orderService, times(1)).getOrderById(1L);
    }

    @Test
    void updateOrderById_ValidInput() throws Exception {
        OrderDto orderDto = OrderDtoGenerator.generateOrderDto();

        Order order = new Order();
        order.setId(orderDto.getId());
        order.setOrderNumber(orderDto.getOrderNumber());

        when(bindingResult.hasErrors()).thenReturn(false);
        when(orderService.getOrderById(ENTITY_ID)).thenReturn(order);

        mockMvc.perform(put("/orders/update/1")
                        .flashAttr("orderDto", orderDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/orders"));

        //verify(orderService, times(1)).getOrderById(orderDto.getId());
        verify(orderService, times(1)).updateOrder(any(Order.class));
        verify(orderService, times(1)).updateOrderDetails(any(OrderDetail.class));
    }

    @Test
    void updateOrderById_InvalidInput() throws Exception {
        OrderDto orderDto = new OrderDto();
        when(bindingResult.hasErrors()).thenReturn(true);

        mockMvc.perform(put("/orders/update/1")
                        .flashAttr("orderDto", orderDto))
                .andExpect(status().isOk())
                .andExpect(view().name("update_order"))
                .andExpect(model().attributeHasErrors("orderDto"));;

        verify(orderService, never()).updateOrder(any(Order.class));
        verify(orderService, never()).updateOrderDetails(any(OrderDetail.class));
    }
}