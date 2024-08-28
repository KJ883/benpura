package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Order;

public interface OrderRepository extends CrudRepository<Order, Integer> {

    @Query("SELECT o FROM order o WHERE o.mailaddress = :mailaddress AND o.date BETWEEN :startDate AND :endDate")
    List<Order> findAllByMailaddressAndDateRange(
        @Param("mailaddress") String mailaddress,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}