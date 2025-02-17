package com.itq.myproject.repository;

import com.itq.myproject.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    //Получение заказа за заданную дату и больше заданной общей суммы заказа
    Optional <Order> findOrderByOrderDateAndTotalSumGreaterThan(LocalDate orderDate, Long totalSum);

    //Получение списка заказов, не содержащих заданный товар и поступивших в заданный временной период

    @Query("SELECT o FROM order o " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
            "AND NOT EXISTS (SELECT 1 FROM orderDetail od WHERE od.order = o AND od.productArticle = :productArticle)")
    List<Order> findOrdersExcludingProductAndBetweenDates(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("productArticle") String productArticle
    );
}

