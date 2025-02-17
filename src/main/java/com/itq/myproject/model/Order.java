package com.itq.myproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"orderDetails"})
@ToString(exclude = {"orderDetails"})
@Table(name = "orders") // говорим Hibernate, с какой именно таблицей необходимо связать (map) данный класс.
@Entity (name = "order")// на этот объект будет мапиться SQL
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="order_number")
    private String orderNumber;

    @Column(name="total_sum")
    private Long totalSum;

    @Column(name="order_date")
    private LocalDate orderDate;

    @Column(name="recipient")
    private String recipient;

    @Column(name="delivery_address")
    private String deliveryAddress;

    @Column(name="payment_type")
    private String paymentType;

    @Column(name="delivery_type")
    private String deliveryType;

    @OneToOne(mappedBy = "order", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private OrderDetail orderDetails;
}
