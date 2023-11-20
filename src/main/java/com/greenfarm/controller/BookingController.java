package com.greenfarm.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.greenfarm.cloudinary.ImageRestController;
import com.greenfarm.dto.BookingDTO;
import com.greenfarm.entity.Booking;
import com.greenfarm.entity.PaymentMethod;
import com.greenfarm.entity.StatusBooking;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.User;
import com.greenfarm.service.BookingService;
import com.greenfarm.service.QRCodeService;
import com.greenfarm.service.StatusBookingService;
import com.greenfarm.service.TourService;
import com.greenfarm.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class BookingController {

	@Autowired
	private UserService userService;

	@Autowired
	BookingService bookingService;

	@Autowired
	StatusBookingService statusBookingService;

	@Autowired
	TourService tourService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private QRCodeService qrCodeService;

	@Autowired
	ImageRestController imageRestController;

	@RequestMapping("/booking/list")
	public String list(Model model, HttpServletRequest request) {
		String email = request.getRemoteUser();
		model.addAttribute("bookings", bookingService.findByEfindByIdAccountmail(email));
		return "booking/mytiket";
	}

	@RequestMapping("/booking/{tourid}")
	public String booking(Model model, HttpServletRequest request, @PathVariable("tourid") Integer tourid) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = userService.findByEmail(userDetails.getUsername());

		Tour item = tourService.findById(tourid);
		Booking booking = new Booking();
		booking.setTour(item);
		booking.setUser(user);
		model.addAttribute("booking", booking);

		return "booking/bookingtour";
	}

	@PostMapping("/booking/create")
	public String createBooking(@ModelAttribute("booking") BookingDTO bookingDto) throws IOException {
		Booking booking = modelMapper.map(bookingDto, Booking.class);
		// Thời gian
		booking.setBookingdate(LocalDateTime.now());

		// Trạng thái
		StatusBooking statusBooking = statusBookingService.findById(1);
		booking.setStatusbooking(statusBooking);

		// Phương thức thanh toán
		PaymentMethod paymentMethod = new PaymentMethod();
		paymentMethod.setPaymentmethodid(1);
		booking.setPaymentmethod(paymentMethod);

		bookingService.saveBooking(booking);
		// Generate and save QR code to the qrcode field
		String qrCodeContent = "http://localhost:8080/rest/bookings/kiemtrave/" + booking.getBookingid();
		System.out.println(qrCodeContent);
		System.out.println(booking.getBookingid());
		String qrCodeUrl = imageRestController.uploadQRCodeToCloud(qrCodeContent);
		// Save the QR code URL to the booking
		booking.setQrcode(qrCodeUrl);

		bookingService.saveBooking(booking);
		return "successboking";
	}
}
