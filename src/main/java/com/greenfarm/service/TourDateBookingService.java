package com.greenfarm.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.greenfarm.dto.TourDTO;
import com.greenfarm.entity.Category;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.TourDateBooking;

public interface TourDateBookingService {

	// API thêm loại sản phẩm
	TourDateBooking create(TourDateBooking tourdatebooking);

	TourDateBooking findById(Integer tourdatebookingid);
	
	// API cập nhật sản phẩm
	TourDateBooking update(TourDateBooking tourdatebooking);

	void deleteTourDateBookingById(Integer tourdatebookingid);
}
