package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Orders;

public interface OrdersRepository extends CrudRepository<Orders, Integer> {

	@Query("SELECT * FROM orders WHERE mailaddress = :mailaddress AND date BETWEEN :startDate AND :endDate")
	List<Orders> findAllByMailaddressAndDateRange(
			@Param("mailaddress") String mailaddress,
			@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);
}