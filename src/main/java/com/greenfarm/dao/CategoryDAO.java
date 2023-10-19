package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.Category;
import com.greenfarm.entity.Tour;

public interface CategoryDAO extends JpaRepository<Category, Integer> {
	

}
