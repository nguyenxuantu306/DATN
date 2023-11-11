package com.greenfarm.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greenfarm.dto.BookingDTO;
import com.greenfarm.dto.OrderDTO;
import com.greenfarm.dto.ProductDTO;
import com.greenfarm.dto.TourDTO;
import com.greenfarm.entity.Booking;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.PaymentMethod;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.StatusBooking;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.User;
import com.greenfarm.service.BookingService;
import com.greenfarm.service.OrderService;
import com.greenfarm.service.StatusBookingService;
import com.greenfarm.service.TourService;
import com.greenfarm.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
	public String createBooking(@ModelAttribute("booking") BookingDTO bookingDto) {
		Booking booking = modelMapper.map(bookingDto, Booking.class);
		//Thời gian 
		booking.setBookingdate(LocalDateTime.now());
		
		//Trạng thái
		StatusBooking statusBooking = statusBookingService.findById(1);
		booking.setStatusbooking(statusBooking);
		
		//Phương thức thanh toán
		PaymentMethod paymentMethod = new PaymentMethod();
		paymentMethod.setPaymentmethodid(1);
		booking.setPaymentmethod(paymentMethod);

		bookingService.saveBooking(booking);
		return "redirect:/booking/success";
	}
	

}
