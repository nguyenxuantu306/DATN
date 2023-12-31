package com.greenfarm.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.greenfarm.dto.TourDTO;
import com.greenfarm.entity.Category;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.TourDate;
import com.greenfarm.entity.TourdateByDate;

public interface TourDateService {

	// API thêm loại sản phẩm
	TourDate create(TourDate tourdate);

	TourDate findById(Integer tourdateid);
	
	
	    
	List<TourDate> findByDate(Date date);

	// API cập nhật sản phẩm
	TourDate update(TourDate tourdate);

	List<TourDate> findByKeywordAndTourName(String keyword);
	
	void deleteTourDateById(Integer tourdateid);
	
	TourDate findByTourAndTourdates(Tour tour, Date tourdates);

	List<TourDate> findAll();

	
	List<TourdateByDate> getTourByDate(Date date);
}
