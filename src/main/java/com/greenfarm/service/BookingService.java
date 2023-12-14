package com.greenfarm.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.JsonNode;
import com.greenfarm.entity.Booking;
import com.greenfarm.entity.FindReportYear;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.ReportYear;
import com.greenfarm.entity.Top3;

public interface BookingService {

	Page<Top3> getTop3Tour(Pageable pageable1);

	List<Booking> findAll();

	List<Booking> getAllBookings(int page, int size);

	Booking create(JsonNode bookingData);

	Booking update(Booking booking);

	List<Booking> findByBookingdateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int page, int size);

	// Thống kê số lượng trạng thái
	List<ReportRevenue> slbookingstatus();

	// Thống kê theo năm của booking
	List<ReportYear> getbookingYearRevenue();

	// Thống kê tìm kiếm theo năm của booking
	List<FindReportYear> findBookingYearlyRevenue(Integer year);

	Booking findById(Integer bookingid);

	List<Booking> findByEfindByIdAccountmail(String email);

	void saveBooking(Booking booking);

}
