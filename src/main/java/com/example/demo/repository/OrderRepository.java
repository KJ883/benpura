package com.example.demo.repository;

import java.time.LocalDateTime;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Order;

public interface OrderRepository extends CrudRepository<Order, Integer>{
	@Query(value="select * order weher mailaddress = :mailaddres and date ")
	Iterable<Order> selectMailaddress(String mailaddress,LocalDateTime date,LocalDateTime sevenDays);

}
