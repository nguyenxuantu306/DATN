package com.greenfarm.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.greenfarm.dto.TourDTO;
import com.greenfarm.entity.Category;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.TourDate;

public interface TourDateService {

	// API thêm loại sản phẩm
	TourDate create(TourDate tourdate);

	TourDate findById(Integer tourdateid);

	// API cập nhật sản phẩm
	TourDate update(TourDate tourdate);

	void deleteTourDateById(Integer tourdateid);
	
	TourDate findByTourAndTourdates(Tour tour, Date tourdates);

	List<TourDate> findAll();
}
