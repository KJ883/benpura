package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekOrderDto {
	private String date;
	private String dayOfWeek;
	private String details;
	private String storeName;
}
