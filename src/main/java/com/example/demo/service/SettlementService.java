package com.example.demo.service;

import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Orders;

@Transactional
public interface SettlementService {
	void insertOrder(Orders order);
}

