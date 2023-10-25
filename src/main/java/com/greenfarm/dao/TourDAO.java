package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenfarm.entity.Product;
import com.greenfarm.entity.Top10;
import com.greenfarm.entity.Tour;

public interface TourDAO extends JpaRepository<Tour, Integer> {

	@Query("SELECT p FROM Tour p WHERE p.Tourname like %?1%")
	List<Tour> findTourByKeyword(String keyword);
	

}
