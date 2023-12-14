package com.greenfarm.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.Tour;
import com.greenfarm.entity.TourImage;

public interface TourImageDAO extends JpaRepository<TourImage, Integer> {

	// TourImage findByTourTourid(Integer tourid);

	void deleteByTourimageid(Integer tourimageid);

	List<TourImage> findByTour(Tour tour);

	Optional<TourImage> findByTourimageid(Integer tourImageId);

	void deleteByTourimageidIn(List<Integer> deleteImageIds);

}
