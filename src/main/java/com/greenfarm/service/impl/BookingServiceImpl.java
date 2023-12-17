package com.greenfarm.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfarm.dao.BookingDAO;
import com.greenfarm.dao.StatusBookingDAO;
import com.greenfarm.entity.Booking;
import com.greenfarm.entity.FindReportYear;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.ReportSP;
import com.greenfarm.entity.ReportTop5Tour;
import com.greenfarm.entity.ReportYear;
import com.greenfarm.entity.StatusBooking;
import com.greenfarm.entity.StatusOrder;
import com.greenfarm.entity.Top3;
import com.greenfarm.service.BookingService;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	BookingDAO dao;

	@Autowired
	StatusBookingDAO statusBookingDAO;

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
	public List<Booking> findByEfindByIdAccountmail(String email) {
		return dao.findByEfindByIdAccountmail(email);
	}

	@Override
	public void saveBooking(Booking booking) {
		dao.save(booking);
	}

	@Override
	public List<Booking> Scantoday(LocalDateTime date) {
		// TODO Auto-generated method stub
		return dao.findByUsedate(date);
	}

	private StatusBooking getCanceledStatusBooking() {
		int canceledStatusBookingid = 4; //
		return statusBookingDAO.getStatusbookingByStatusbookingid(canceledStatusBookingid);
	}

	@Override
	public void cancelBooking(Integer bookingid) {
		Optional<Booking> optionalBooking = dao.findById(bookingid);
		if (optionalBooking.isPresent()) {
			Booking booking = optionalBooking.get();
			// Kiểm tra trạng thái đơn hàng trước khi hủy
			if (booking.getStatusbooking().getStatusbookingid() == 1) {
				// Cập nhật trạng thái đơn hàng thành "Đã hủy"
				StatusBooking canceledStatus = getCanceledStatusBooking(); // Lấy trạng thái "Đã hủy" từ cơ sở dữ
																			// liệu

				if (canceledStatus != null) {
					// Cập nhật trạng thái của đơn hàng thành "Đã hủy"
					booking.setStatusbooking(canceledStatus);
					dao.save(booking);
				} else {
					System.out.println(booking.getStatusbooking().getStatusbookingid());
					System.out.println(canceledStatus.getStatusbookingid());
					throw new RuntimeException("Không thể hủy tour.");
				}
			} else {
				throw new RuntimeException("Không tìm tour.");
			}
		}
	}

	@Override
	public List<ReportTop5Tour> gettourdatNT() {
		List<ReportTop5Tour> toursBygetReporttourbanchay = dao.getTop5ToursBygetReporttourdatNT();
		if (toursBygetReporttourbanchay.size() > 5) {
			return toursBygetReporttourbanchay.subList(0, 5);
		} else {
			return toursBygetReporttourbanchay;
		}
	}
}
