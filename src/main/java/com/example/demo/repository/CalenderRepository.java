package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Calender;

public interface CalenderRepository extends CrudRepository<Calender,Integer> {
	
}
