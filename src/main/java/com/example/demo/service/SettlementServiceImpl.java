package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Orders;
import com.example.demo.repository.CalendarRepository;
import com.example.demo.repository.OrdersRepository;
@Service
@Transactional
public class SettlementServiceImpl implements SettlementService {
	@Autowired
	OrdersRepository orderRepository;
	@Autowired
	CalendarRepository calendarRepository;

	@Override
	public void insertOrder(Orders order) {
		// 注文履歴テーブルに追加
		orderRepository.save(order);
	}

	
	
	
	
	

}
