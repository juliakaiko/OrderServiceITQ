package com.itq.myproject.controller;

import com.itq.myproject.annotation.MyExceptionHandler;
import com.itq.myproject.dto.FilterBySumDto;
import com.itq.myproject.dto.FilterExcludingProductDto;
import com.itq.myproject.dto.OrderDto;
import com.itq.myproject.mapper.OrderDetailMapper;
import com.itq.myproject.mapper.OrderMapper;
import com.itq.myproject.model.Order;
import com.itq.myproject.model.OrderDetail;
import com.itq.myproject.service.NumberGenerateService;
import com.itq.myproject.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
@Tag(name="OrderController") // для swagger-а
@MyExceptionHandler
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final NumberGenerateService numberGenerateService;

    @GetMapping("/orders")
    public String showOrderList(Model model) {
        model.addAttribute("orders", orderService.findAllOrders());
        return "order-list";
    }

    @GetMapping("/orders/{id}")
    public String getOrderById(@PathVariable Long id, Model model) {
        model.addAttribute("orderDto", orderService.getOrderById(id));
        return "order";
    }

    @GetMapping("/orders/detail/{id}")
    public String getOrderDetailById(@PathVariable Long id, Model model) {
        model.addAttribute("detail", orderService.getOrderDetailById(id));
        return "detail";
    }

    @GetMapping("/orders/details")
    public String showOrderDetailList(Model model) {
        model.addAttribute("details", orderService.findAllDetailOrders());
        return "detail-list";
    }

    @GetMapping("/orders/add")
    public String showAddForm(Model model) {
        OrderDto orderDto = new OrderDto();
        model.addAttribute("order", orderDto);
        return "add_order";
    }

    @PostMapping("/orders/add")
    public String createOrder(@Valid @ModelAttribute("order") OrderDto orderDto, BindingResult result,Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "add_order";
        }
        Order order = OrderMapper.INSTANSE.toEntity(orderDto);
        OrderDetail orderDetail = OrderDetailMapper.INSTANSE.toEntity(orderDto);
        //Сгенерировать уникальный номер заказа
        order.setOrderNumber(numberGenerateService.generateOrderNumber(order.getOrderDate()));
        order.setOrderDetails(orderDetail);
        orderDetail.setOrder(order);
        orderService.createOrderDetails(orderDetail);
        orderService.createOrder(order);
        return "redirect:/orders";
    }

   // Получение заказа за заданную дату и больше заданной общей суммы заказа
    @GetMapping("/orders/filter_by_date_and_sum")
    public String showFilterFormOne(Model model) {
        FilterBySumDto orderDto = new FilterBySumDto();
        model.addAttribute("orderDto", orderDto);
        return "filter_form_1";
    }

    @PostMapping("/orders/filter_by_date_and_sum")
    public String getOrderByOrderDateAndTotalSumGreaterThan(
            @Valid @ModelAttribute("orderDto") FilterBySumDto orderDto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("orderDto", orderDto);
            return "filter_form_1";
        }

        LocalDate orderDate = orderDto.getOrderDate();
        Long totalSum = orderDto.getTotalSum();

        model.addAttribute("orderDto", orderService.findOrderByOrderDateAndTotalSumGreaterThan(orderDate, totalSum));
        return "order";
    }

    //Получение списка заказов, не содержащих заданный товар и поступивших в заданный временной период.
    @GetMapping("/orders/filter_between_dates_and_excluding_product")
    public String showFilterFormTwo(Model model) {
        FilterExcludingProductDto orderDto = new FilterExcludingProductDto();
        model.addAttribute("orderDto", orderDto);
        return "filter_form_2";
    }

    @PostMapping("/orders/filter_between_dates_and_excluding_product")
    public String findOrdersBetweenStartDateAndEndDateANDExcludingProduct(
            @Valid  @ModelAttribute FilterExcludingProductDto filterDto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("orderDto", filterDto);
            return "filter_form_2";
        }

        LocalDate startDate = filterDto.getStartDate();
        LocalDate endDate = filterDto.getEndDate();
        String productArticle = filterDto.getProductArticle();

        model.addAttribute("orders",
                orderService.findOrdersExcludingProductAndBetweenDates(startDate, endDate, productArticle));
        return "order-list";
    }

    @DeleteMapping("/orders/delete/{id}")
    public String deleteOrderById(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return "redirect:/orders";
    }

    @GetMapping("/orders/update/{id}")
    public String showUpdateForm(@PathVariable Long id,Model model) {
        Order order = orderService.getOrderById(id);
        OrderDto orderDto = new OrderDto();
        orderDto.setId(id);
        orderDto.setOrderNumber(order.getOrderNumber());
        model.addAttribute("orderDto", orderDto);
        return "update_order";
    }

    @PutMapping("/orders/update/{id}")
    public String updateOrderById(@PathVariable Long id,
                                  @Valid @ModelAttribute ("orderDto") OrderDto orderDto,
                                  BindingResult result,
                                  Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("orderDto", orderDto);
            return "update_order";
        }
        //Mapping
        Order order = OrderMapper.INSTANSE.toEntity(orderDto);
        OrderDetail orderDetail = OrderDetailMapper.INSTANSE.toEntity(orderDto);

        Order orderFromDb = orderService.getOrderById(id);
        order.setOrderNumber(orderFromDb.getOrderNumber());
        order.setId(id);
        order.setOrderDetails(orderDetail);
        orderDetail.setOrder(order);

        orderService.updateOrderDetails(orderDetail);
        orderService.updateOrder(order);
        return "redirect:/orders";
    }
}
