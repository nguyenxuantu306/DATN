package com.greenfarm.service;

import java.util.Date;
import java.util.List;

import com.greenfarm.entity.TourDateBooking;

public interface TourDateBookingService {

	// API thêm loại sản phẩm
	TourDateBooking create(TourDateBooking tourdatebooking);

	List<TourDateBooking> findByKeyword(String keyword);
	
	TourDateBooking findById(Integer tourdatebookingid);
	
	List<TourDateBooking> findByDate(Date date);
	
	// API cập nhật sản phẩm
	TourDateBooking update(TourDateBooking tourdatebooking);

	void deleteTourDateBookingById(Integer tourdatebookingid);

	List<TourDateBooking> findAll();

	List<TourDateBooking> findByDepartureDay(String departureday);

	int getBookedSlotsForTourDate(TourDate tourDate);

	List<TourDateBooking> getBookingsForTourDate(TourDate tourdate);
}
