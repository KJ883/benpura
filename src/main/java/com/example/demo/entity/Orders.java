package com.example.demo.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Orders {
	@Id
	private Integer id;
	private String mailaddress;
	private String shopname;
	private String item;
	private Integer price;
	private LocalDateTime date;
}
