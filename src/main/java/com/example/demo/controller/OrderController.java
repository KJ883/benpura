package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Orders;
import com.example.demo.repository.OrdersRepository;

@RestController
public class OrderController {

    @Autowired
    private OrdersRepository orderRepository;

    @GetMapping("/api/orders/week")
    public ResponseEntity<Map<String, List<Orders>>> getWeekOrders(Authentication authentication) {
        String mailaddress = authentication.getName();
        LocalDate today = LocalDate.now();
        Map<String, List<Orders>> weekOrders = new HashMap<>();

        // 一週間分の注文をまとめて取得
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfWeek = today.plusDays(6).atTime(23, 59, 59);
        Iterable<Orders> allOrders = orderRepository.findAllByMailaddressAndDateRange(
            mailaddress, startOfDay, endOfWeek
        );

        // 取得した注文を日付ごとに分類
        Map<LocalDate, List<Orders>> ordersByDate = StreamSupport.stream(allOrders.spliterator(), false)
            .collect(Collectors.groupingBy(order -> order.getDate().toLocalDate()));

        // 今日から一週間分の日付をループして、注文を曜日ごとにマップに追加
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.plusDays(i);
            String formattedDate = date.toString();
            weekOrders.put(formattedDate, ordersByDate.getOrDefault(date, List.of()));
        }

        return ResponseEntity.ok(weekOrders);
    }
}
