package com.greenfarm.service;

import java.util.List;

import com.greenfarm.entity.TourImage;

public interface TourImageService {

	List<TourImage> findAll();

	void createAll(List<TourImage> tourImages);

//	TourImage findByTourId(Integer tourid);

	void create(List<TourImage> tourImages);

	List<TourImage> findByTourTourid(Integer tourid);

	void save(TourImage tourImage);

}
