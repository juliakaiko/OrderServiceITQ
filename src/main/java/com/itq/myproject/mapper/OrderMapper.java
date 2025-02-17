package com.itq.myproject.mapper;

import com.itq.myproject.dto.OrderDto;
import com.itq.myproject.model.Order;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANSE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "id", source = "order.id")
    @Mapping(target = "orderNumber", source = "order.orderNumber")
    @Mapping(target = "totalSum", source = "order.totalSum")
    @Mapping(target = "orderDate", source = "order.orderDate")
    @Mapping(target = "recipient", source = "order.recipient")
    @Mapping(target = "deliveryAddress", source = "order.deliveryAddress")
    @Mapping(target = "paymentType", source = "order.paymentType")
    @Mapping(target = "deliveryType", source = "order.deliveryType")
    OrderDto toDto(Order order);

    @InheritInverseConfiguration  // преобразует обратно DTO в Entity
    Order toEntity (OrderDto orderDto);
}
