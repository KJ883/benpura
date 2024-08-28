package com.example.demo.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="orders") // 呼び出したいDBのテーブル名を指定
//
//  entityパッケージ
//  Calenderクラスの生成
//
public class Calendar {
	
//	Idはusernameに
//	DBも主キーをusernameに?
	
	@Id
	private Integer id;
	private String mailaddress;
	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
	private LocalDate date;
	private String shopname;
	private String item;
	private Integer price;
	
	
}