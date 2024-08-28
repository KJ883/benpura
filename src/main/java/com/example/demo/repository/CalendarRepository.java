package com.example.demo.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Calendar;

public interface CalendarRepository extends CrudRepository<Calendar,Integer> {
	
	// 注文履歴を全件取得 (SQLクエリで注文日時を昇降順に並び替え)
	@Query(value="SELECT * FROM \"orders\" ORDER BY date desc;")
	Iterable<Calendar>selectAll();

	@Query(value = "select * from orders weher mailaddress=:mailaddress")
	Iterable<Calendar> findById(String mailaddress);

	
	
}

