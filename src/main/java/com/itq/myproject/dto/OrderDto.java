package com.itq.myproject.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class OrderDto {

    private Long id;
    private String orderNumber;
    private Long totalSum;

    @NotNull(message = "Date may not be empty")
    @Past(message = "Date cannot be in the future")
    private LocalDate orderDate;

    @NotBlank(message = "The recipient may not be anonymous")
    private String recipient;

    @NotBlank(message = "The delivery address is not provided")
    private String deliveryAddress;

    @NotBlank(message = "The payment method is not selected")
    private String paymentType;

    @NotBlank(message = "The delivery type is not selected")
    private String deliveryType;

    //OrderDetail
    @NotBlank(message = "The product article is not specified")
    private String productArticle;

    @NotBlank(message = "The product name is not specified")
    private String productName;

    @NotNull(message = "Quantity may not be null")
    @Min(value = 1, message = "Incorrect quantity")
    private Long quantity;

    @NotNull(message = "Price may not be null")
    @Min(value = 10, message = "Incorrect price")
    @Max(value = 10000, message = "Incorrect price")
    private Long unitPrice;

    public Long getTotalSum() {
        if(quantity!=null & unitPrice!=null)
            this.totalSum = this.quantity*this.unitPrice;
        else
            totalSum = 0l;
        return totalSum;
    }
}
