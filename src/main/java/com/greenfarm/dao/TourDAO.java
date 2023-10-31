package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.Tour;

public interface TourDAO extends JpaRepository<Tour, Integer> {

	@Query("SELECT p FROM Tour p WHERE p.Tourname like %?1%")
	List<Tour> findTourByKeyword(String keyword);
	

}
