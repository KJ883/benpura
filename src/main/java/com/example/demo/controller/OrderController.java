package com.example.demo.controller;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @GetMapping("/api/orders/week")
    public Map<String, List<Order>> getWeeklyOrders() {
        Map<String, List<Order>> ordersMap = new HashMap<>();
        LocalDate today = LocalDate.now();

        // サンプルデータの生成
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.plusDays(i);
            String dateStr = date.toString();
            String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.JAPANESE);
            
            List<Order> orders = new ArrayList<>();
            for (int j = 0; j < 2; j++) {  // 各日付に2つの注文を追加
                orders.add(new Order(dateStr, dayOfWeek, "注文内容 " + (i + 1) + "-" + (j + 1), "店名 " + (i + 1) + "-" + (j + 1)));
            }
            ordersMap.put(dateStr, orders);
        }

        return ordersMap;
    }

    static class Order {
        private String date;
        private String dayOfWeek;
        private String details;
        private String storeName;

        public Order(String date, String dayOfWeek, String details, String storeName) {
            this.date = date;
            this.dayOfWeek = dayOfWeek;
            this.details = details;
            this.storeName = storeName;
        }

        // Getter and Setter
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(String dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }
    }
}
