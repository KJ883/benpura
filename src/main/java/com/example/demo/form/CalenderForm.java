package com.example.demo.form;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalenderForm {
	
	@Id
	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
	private LocalDate orderdate;

//	private String dayofweek;
	
}