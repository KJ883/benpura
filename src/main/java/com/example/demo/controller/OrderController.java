package com.example.demo.controller;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.WeekOrderDto;

import jakarta.servlet.http.HttpSession;

@RestController
public class OrderController {
private HttpSession session;
@Autowired
public OrderController(HttpSession session) {
	// フィールドに代入する
	this.session = session;
}
	@GetMapping("/api/orders/week")
	public List<WeekOrderDto> getWeeklyOrders() {
		List<WeekOrderDto> orders = new ArrayList<>();
		LocalDateTime today = (LocalDateTime) this.session.getAttribute("orderdatetime");

		// サンプルデータの生成
		for (int i = 0; i < 7; i++) {
			LocalDateTime date = today.plusDays(i);
			String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.JAPANESE);
			orders.add(new WeekOrderDto(date.toString(), dayOfWeek, "注文内容 " + (i + 1), this.session.getAttribute("shopName").toString()));
		}

		return orders;
	}
}
//	static class Order {
//		private String date;
//		private String dayOfWeek;
//		private String details;
//		private String storeName;
//
//		public Order(String date, String dayOfWeek, String details, String storeName) {
//			this.date = date;
//			this.dayOfWeek = dayOfWeek;
//			this.details = details;
//			this.storeName = storeName;
//		}
//
//		// Getter and Setter
//		public String getDate() {
//			return date;
//		}
//
//		public void setDate(String date) {
//			this.date = date;
//		}
//
//		public String getDayOfWeek() {
//			return dayOfWeek;
//		}
//
//		public void setDayOfWeek(String dayOfWeek) {
//			this.dayOfWeek = dayOfWeek;
//		}
//
//		public String getDetails() {
//			return details;
//		}
//
//		public void setDetails(String details) {
//			this.details = details;
//		}
//
//		public String getStoreName() {
//			return storeName;
//		}
//
//		public void setStoreName(String storeName) {
//			this.storeName = storeName;
//		}
//	}
//}
