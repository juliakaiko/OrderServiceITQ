package com.itq.myproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"order"})
@ToString(exclude = {"order"})
@Table(name = "order_details") // говорим Hibernate, с какой именно таблицей необходимо связать (map) данный класс.
@Entity (name = "orderDetail")// на этот объект будет мапиться SQL
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="article")
    private String productArticle;

    @Column(name="name")
    private String productName;

    @Column(name="quantity")
    private Long quantity;

    @Column(name="unit_price")
    private Long unitPrice;

    @OneToOne (cascade = {CascadeType.MERGE, CascadeType.PERSIST},fetch = FetchType.LAZY)//(fetch = FetchType.LAZY)
    @MapsId // Связывает идентификаторы с сущностью Order
    @JsonIgnore
    private Order order;
}
