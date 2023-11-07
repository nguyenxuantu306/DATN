package com.greenfarm.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfarm.dao.BookingDAO;
import com.greenfarm.entity.Booking;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.ReportRevenue;
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
}
