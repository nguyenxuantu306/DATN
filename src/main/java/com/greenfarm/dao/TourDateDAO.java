package com.greenfarm.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.Tour;

import java.time.LocalDate;
import java.util.Date;


public interface TourDateDAO extends JpaRepository<TourDate, Integer> {
	
	
	List<TourDate> findByTourAndTourdates(Tour tour, Date tourdates);
	
	
	@Query("SELECT p FROM TourDate p WHERE p.availableslots = :keyword")
	List<TourDate> findByKeyword(@Param("keyword") Integer keyword);

	@Query("SELECT p FROM TourDate p WHERE DATE(p.tourdates) = :date")
	List<TourDate> findByTourdates(@Param("date") Date date);




}
