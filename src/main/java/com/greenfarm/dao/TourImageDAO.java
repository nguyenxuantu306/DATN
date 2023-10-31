package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.TourImage;

public interface TourImageDAO extends JpaRepository<TourImage, Integer> {

	List<TourImage> findByTourTourid(Integer tourid);
	//TourImage findByTourTourid(Integer tourid);

}
