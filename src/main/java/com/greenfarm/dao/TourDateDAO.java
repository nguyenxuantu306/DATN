package com.greenfarm.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.Tour;
import com.greenfarm.entity.TourDate;


public interface TourDateDAO extends JpaRepository<TourDate, Integer> {
	
	
//	@Query("SELECT * FROM tourdate WHERE tourid = ? AND tourdates = ?")
//	List<TourDate> findByTourandtourdatesDates();
	
	List<TourDate> findByTourAndTourdates(Tour tour, Date tourdates);
}
