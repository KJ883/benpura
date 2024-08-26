package com.example.demo.service;

import com.example.demo.entity.ShopList;

public interface ShopListService {

	Iterable<ShopList> selectAll();
	// Formクラスを使って注文日時の引継ぎと曜日の引き渡し
	
	
}
