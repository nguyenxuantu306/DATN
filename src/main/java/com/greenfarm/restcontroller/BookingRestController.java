package com.greenfarm.restcontroller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.greenfarm.dto.BookingDTO;
import com.greenfarm.dto.OrderDTO;
import com.greenfarm.dto.ProductDTO;
import com.greenfarm.entity.Booking;
import com.greenfarm.entity.FindReportYear;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.ReportYear;
import com.greenfarm.entity.RevenueTK;
import com.greenfarm.service.BookingService;
import com.greenfarm.service.OrderDetailService;
import com.greenfarm.service.OrderService;
import com.greenfarm.service.ProductService;
import com.greenfarm.service.TourService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/bookings")
public class BookingRestController {
	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private TourService tourService;

	@Autowired
	ModelMapper modelMapper;


	@GetMapping()
	public ResponseEntity<List<BookingDTO>> getList() {
		List<Booking> bookings = bookingService.findAll();

		// Sử dụng ModelMapper để ánh xạ từ danh sách Product sang danh sách ProductDTO
		List<BookingDTO> BookingsDTOs = bookings.stream().map(booking -> modelMapper.map(booking, BookingDTO.class))
				.collect(Collectors.toList());

		// Trả về danh sách ProductDTO bằng ResponseEntity với mã trạng thái 200 OK
		return new ResponseEntity<>(BookingsDTOs, HttpStatus.OK);
	}
	
	@GetMapping("{bookingid}")
	public ResponseEntity<BookingDTO> getOne(@PathVariable("bookingid") Integer bookingid) {
		Booking booking = bookingService.findById(bookingid);
		BookingDTO bookingDTOs = modelMapper.map(booking, BookingDTO.class);
		return new ResponseEntity<>(bookingDTOs, HttpStatus.OK);
	}

	public ResponseEntity<String> getAllBookings(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		List<Booking> bookings = bookingService.getAllBookings(page, size);
		List<BookingDTO> BookingsDTOs = bookings.stream().map(booking -> modelMapper.map(booking, BookingDTO.class))
				.collect(Collectors.toList());

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String json = objectMapper.writeValueAsString(BookingsDTOs);
			return ResponseEntity.ok(json);
		} catch (JsonProcessingException e) {
			// Xử lý lỗi nếu chuyển đổi sang JSON không thành công
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}


	@PostMapping()
	public Booking create(@RequestBody JsonNode bookingData) {
		return bookingService.create(bookingData);
	}

	@PutMapping("{id}")
	public ResponseEntity<BookingDTO> update(@PathVariable("id") Integer id,
			@RequestBody Booking booking) {
		Booking updatedBooking = bookingService.update(booking);

		if (updatedBooking == null) {
			return ResponseEntity.notFound().build();
		}
		BookingDTO bookingDTO = modelMapper.map(updatedBooking, BookingDTO.class);
		return new ResponseEntity<>(bookingDTO, HttpStatus.OK);
	}
	
	@GetMapping("/search")
	public ResponseEntity<String> searchBookingsByDate(
			@RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime startDateTime,
			@RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime endDateTime,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		List<Booking> bookings = bookingService.findByBookingdateBetween(startDateTime, endDateTime, page, size);

		List<BookingDTO> bookingDTOs = bookings.stream().map(booking -> modelMapper.map(booking, BookingDTO.class))
				.collect(Collectors.toList());

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		try {
			String json = objectMapper.writeValueAsString(bookingDTOs);
			return ResponseEntity.ok(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// Thống kê số lượng trạng thái
	@GetMapping("/slbookingstatus")
	public ResponseEntity<List<ReportRevenue>> slbookingstatus() {
		return new ResponseEntity<>(bookingService.slbookingstatus(), HttpStatus.OK);
	}
	
	@GetMapping("/bookingyear-revenue")
	public ResponseEntity<List<ReportYear>> getbookingyearRevenue() {
	      List<ReportYear> yearRevenue = bookingService.getbookingYearRevenue();
	      return new ResponseEntity<>(yearRevenue, HttpStatus.OK);
	}
	
	@GetMapping("/findbookingyearrevenue/{year}")
    public List<FindReportYear> getBookingYearlyRevenue(@PathVariable Integer year) {
       return bookingService.findBookingYearlyRevenue(year);
	 }
	
}
