package com.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.money.api.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	

}
