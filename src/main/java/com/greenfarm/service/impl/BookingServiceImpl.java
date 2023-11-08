package com.greenfarm.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfarm.dao.BookingDAO;
import com.greenfarm.entity.Booking;
import com.greenfarm.entity.FindReportYear;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.ReportYear;
import com.greenfarm.entity.Top3;
import com.greenfarm.service.BookingService;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	BookingDAO dao;

	@Override
	public Page<Top3> getTop3Tour(Pageable pageable1) {
		return dao.getTop3Tour(pageable1);
	}

	@Override
	public List<Booking> findAll() {
		return dao.findAll();
	}

	@Override
	public List<Booking> getAllBookings(int page, int size) {
		int offset = page * size;
		return dao.findAll(offset, size);
	}

	@Override
	public Booking create(JsonNode bookingData) {
		ObjectMapper mapper = new ObjectMapper();

		Booking booking = mapper.convertValue(bookingData, Booking.class);
		dao.save(booking);
		return booking;
	}

	
	@Override
	public Booking update(Booking booking) {
		return dao.save(booking);
	}
	
	@Override
	public List<Booking> findByBookingdateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int page,
			int size) {
		Pageable pageable = PageRequest.of(page, size);
        return dao.findByBookingdateBetween(startDateTime, endDateTime, pageable);
	}

	@Override
	public List<ReportRevenue> slbookingstatus() {
		return dao.countBookingsByStatus();
	}

	@Override
	public List<ReportYear> getbookingYearRevenue() {
		return dao.getbookingYearRevenue();
	}

	@Override
	public List<FindReportYear> findBookingYearlyRevenue(Integer year) {
		return dao.findBookingYearlyRevenue(year);
	}

	
	public Booking findById(Integer bookingid) {
		return dao.findById(bookingid).get();
	}

	@Override
	public void saveBooking(Booking booking) {
		dao.save(booking);
	}
}
