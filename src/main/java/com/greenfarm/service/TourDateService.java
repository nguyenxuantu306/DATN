package com.greenfarm.service;

import java.util.Date;
import java.util.List;

import com.greenfarm.entity.Tour;
import com.greenfarm.entity.TourDate;

public interface TourDateService {

	// API thêm loại sản phẩm
	TourDate create(TourDate tourdate);

	TourDate findById(Integer tourdateid);
	
	
	    
	List<TourDate> findByDate(Date date);

	// API cập nhật sản phẩm
	TourDate update(TourDate tourdate);

	List<TourDate> findByKeyword(Integer keyword);
	
	void deleteTourDateById(Integer tourdateid);

	TourDate findByTourAndTourdates(Tour tour, Date tourdates);

	List<TourDate> findAll();
}
