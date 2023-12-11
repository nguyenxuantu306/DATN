package com.greenfarm.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greenfarm.cloudinary.ImageRestController;
import com.greenfarm.dto.BookingDTO;
import com.greenfarm.entity.Booking;
import com.greenfarm.entity.PaymentMethod;
import com.greenfarm.entity.StatusBooking;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.TourDate;
import com.greenfarm.entity.TourDateBooking;
import com.greenfarm.entity.User;
import com.greenfarm.service.BookingService;
import com.greenfarm.service.StatusBookingService;
import com.greenfarm.service.TourDateBookingService;
import com.greenfarm.service.TourDateService;
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
	TourDateService tourdateService;

	@Autowired
	TourDateBookingService tourdatebookingService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	ImageRestController imageRestController;

	@RequestMapping("/booking/list")
	public String list(Model model, HttpServletRequest request,
			@RequestParam(name = "statusFilter", required = false) String statusFilter) {
		String email = request.getRemoteUser();
		List<Booking> bookings = bookingService.findByEfindByIdAccountmail(email);

		// Filter bookings based on status
		if (statusFilter != null && !statusFilter.isEmpty()) {
			bookings = bookings.stream().filter(booking -> booking.getStatusbooking().getName().equals(statusFilter))
					.collect(Collectors.toList());
		}

		model.addAttribute("bookings", bookings);
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
	public String createBooking(Model model, @ModelAttribute("booking") BookingDTO bookingDto,
			@RequestParam("tourdate") String tourdate, BindingResult bindingResult) throws IOException, ParseException {
		Booking booking = modelMapper.map(bookingDto, Booking.class);

		if (bindingResult.hasErrors()) {
			model.addAttribute("booking", bookingDto);
			// Trả về trang form với thông báo lỗi
			return "bookingform";
		}
		
		Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(tourdate);
		TourDate tourdate1 = tourdateService.findByTourAndTourdates(booking.getTour(), date1);
		
		// Kiểm tra số lượng slot khả dụng
		int availableSlots = getAvailableSlots(tourdate1);
		System.out.println(availableSlots);
		int requestedSlots = booking.getAdultticketnumber() + booking.getChildticketnumber();
		System.out.println(requestedSlots);
		
		if (availableSlots < requestedSlots) {
			// Số lượng slot không đủ, trả về trang form với thông báo lỗi
			model.addAttribute("booking", bookingDto);
			model.addAttribute("error", "Số lượng slot không đủ cho đặt hàng này.");
			System.out.println("ko đủ số lượng");
			return "redirect:/booking/" + booking.getTour().getTourid();
		} else {
			

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

			TourDateBooking tourdatebooking = new TourDateBooking();
			tourdatebooking.setBooking(booking);
			tourdatebooking.setTourdate(tourdate1);
			tourdatebookingService.create(tourdatebooking);

			// Generate and save QR code to the qrcode field
			String qrCodeContent = "" + booking.getBookingid();
			System.out.println(qrCodeContent);
			System.out.println(booking.getBookingid());
			String qrCodeUrl = imageRestController.uploadQRCodeToCloud(qrCodeContent);
			// Save the QR code URL to the booking
			booking.setQrcode(qrCodeUrl);

			bookingService.saveBooking(booking);
			return "successboking";
		}
	}

	public int getAvailableSlots(TourDate tourDate) {
		// Giả sử trong TourDate có một trường là availableSlots để biểu diễn số lượng
		// slot khả dụng
		int availableSlots = tourDate.getAvailableslots();

		// Lấy danh sách TourDateBooking cho tourdate cụ thể
		List<TourDateBooking> tourDateBookings = tourdatebookingService.getBookingsForTourDate(tourDate);

		// Tính tổng số lượng slot đã đặt
		int bookedSlots = tourDateBookings.stream().mapToInt(
				booking -> booking.getBooking().getAdultticketnumber() + booking.getBooking().getChildticketnumber())
				.sum();

		// Tính số lượng slot khả dụng
		int remainingSlots = availableSlots - bookedSlots;

		// Đảm bảo số lượng slot không dưới 0
		return Math.max(remainingSlots, 0);
	}

}
