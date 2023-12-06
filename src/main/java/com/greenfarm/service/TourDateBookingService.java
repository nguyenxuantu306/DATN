package com.greenfarm.service;

import com.greenfarm.entity.TourDateBooking;

public interface TourDateBookingService {

	// API thêm loại sản phẩm
	TourDateBooking create(TourDateBooking tourdatebooking);

	TourDateBooking findById(Integer tourdatebookingid);
	
	// API cập nhật sản phẩm
	TourDateBooking update(TourDateBooking tourdatebooking);

	void deleteTourDateBookingById(Integer tourdatebookingid);

	List<TourDateBooking> findAll();
}
