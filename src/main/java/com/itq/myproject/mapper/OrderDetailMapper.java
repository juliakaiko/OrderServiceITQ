package com.itq.myproject.mapper;

import com.itq.myproject.dto.OrderDto;
import com.itq.myproject.model.OrderDetail;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderDetailMapper {

    OrderDetailMapper INSTANSE = Mappers.getMapper(OrderDetailMapper.class);

    @Mapping(target = "id", source = "orderDetails.id")
    @Mapping(target = "productArticle", source = "orderDetails.productArticle")
    @Mapping(target = "productName", source = "orderDetails.productName")
    @Mapping(target = "quantity", source = "orderDetails.quantity")
    @Mapping(target = "unitPrice", source = "orderDetails.unitPrice")
    OrderDto toDto(OrderDetail orderDetails);

    @InheritInverseConfiguration // преобразует обратно DTO в Entity
    OrderDetail toEntity (OrderDto orderDto);
}
