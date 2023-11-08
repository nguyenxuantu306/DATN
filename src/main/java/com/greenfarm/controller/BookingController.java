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
import com.greenfarm.entity.Booking;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.StatusBooking;
import com.greenfarm.service.BookingService;
import com.greenfarm.service.OrderService;
import com.greenfarm.service.StatusBookingService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
public class BookingController {

	@Autowired
	BookingService bookingService;
	
	@Autowired
	StatusBookingService statusBookingService;

	@Autowired
	ModelMapper modelMapper;

	@RequestMapping("/booking/list")
	public String list(Model model, HttpServletRequest request) {
		String email = request.getRemoteUser();
		model.addAttribute("bookings", bookingService.findByEfindByIdAccountmail(email));
		return "booking/mytiket";
	}	
	
	@RequestMapping("/booking")
	public String booking(Model model, HttpServletRequest request) {
		
		return "booking/bookingtour";
	}	

	//Tự viết đi nhé
	@PostMapping("/booking/create")
	public String createBooking(@ModelAttribute("bookingDto") BookingDTO bookingDto) {
	    Booking booking = modelMapper.map(bookingDto, Booking.class);
	    booking.setBookingdate(LocalDateTime.now());
	    StatusBooking statusBooking = statusBookingService.findById(1); 
	    booking.setStatusbooking(statusBooking);
	    bookingService.saveBooking(booking);
	    return "redirect:/booking/success";
	}
}
