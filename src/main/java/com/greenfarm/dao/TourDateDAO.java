package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.greenfarm.entity.Category;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.TourDate;
import com.greenfarm.entity.Tour;

import java.time.LocalDate;
import java.util.Date;


public interface TourDateDAO extends JpaRepository<TourDate, Integer> {
	
	
	List<TourDate> findByTourAndTourdates(Tour tour, Date tourdates);
	
	
	@Query("SELECT p FROM TourDate p WHERE p.Availableslots = :keyword")
	List<TourDate> findByKeyword(@Param("keyword") Integer keyword);

	@Query("SELECT p FROM TourDate p WHERE DATE(p.tourdates) = :date")
	List<TourDate> findByTourdates(@Param("date") LocalDate date);




}