package com.greenfarm.service;

import java.util.List;

import com.greenfarm.entity.Tour;
import com.greenfarm.entity.TourImage;

public interface TourImageService {

	List<TourImage> findAll();

	void createAll(List<TourImage> tourImages);

//	TourImage findByTourId(Integer tourid);

	void create(List<TourImage> tourImages);

	TourImage save(TourImage tourImage);

//	void delete(TourImage tourImageId);

	void deleteByTourimageid(Integer tourimageid);

	List<TourImage> findByTour(Tour tour);

	/* TourImage findByTourimageid(Integer tourImageId); */

	void update(TourImage tourImage);

//	void delete(Integer tourImageId);

	void delete(TourImage tourImage);

	void deleteById(Integer tourimageid);
}
