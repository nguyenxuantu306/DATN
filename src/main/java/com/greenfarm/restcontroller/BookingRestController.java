package com.greenfarm.restcontroller;

import java.io.FileNotFoundException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.greenfarm.controller.MailControl;
import com.greenfarm.dto.BookingDTO;
import com.greenfarm.entity.Booking;
import com.greenfarm.entity.FindReportYear;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.ReportYear;
import com.greenfarm.entity.Role;
import com.greenfarm.entity.StatusBooking;
import com.greenfarm.entity.User;
import com.greenfarm.entity.UserRole;
import com.greenfarm.service.BookingService;
import com.greenfarm.service.EmailService;
import com.greenfarm.service.StatusBookingService;
import com.greenfarm.service.UserService;
import com.greenfarm.service.impl.BookingConfirmEmailContext;

import jakarta.mail.MessagingException;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/bookings")
public class BookingRestController {
	@Autowired
	private BookingService bookingService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	EmailService emailService;

	@Autowired
	UserService userService;

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

	@PutMapping("{id}")
	public ResponseEntity<BookingDTO> update(@PathVariable("id") Integer id, @RequestBody Booking booking) {
		Booking updatedBooking = bookingService.update(booking);

		if (updatedBooking == null) {
			return ResponseEntity.notFound().build();
		}
		BookingDTO bookingDTO = modelMapper.map(updatedBooking, BookingDTO.class);
		return new ResponseEntity<>(bookingDTO, HttpStatus.OK);
	}
	

	@PutMapping("/cancel/{bookingid}")
	public ResponseEntity<String> cancelBooking(@PathVariable("bookingid") Integer bookingid) {
		try {
			bookingService.cancelBooking(bookingid);
			return new ResponseEntity<>("Đã hủy tour thành công", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Lỗi khi hủy tour: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
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

	private static final Logger LOG = LoggerFactory.getLogger(MailControl.class);

	@GetMapping("/sendbooking/{bookingid}")
	public void sendbooking(@PathVariable("bookingid") Integer bookingid)
			throws MessagingException, FileNotFoundException {
		System.out.println("Gui mail den khach hang");
		Booking booking = bookingService.findById(bookingid);

		// Lấy đường dẫn URL của hình ảnh QR code từ cơ sở dữ liệu
		String qrCodeUrl = booking.getQrcode();

		System.out.println(qrCodeUrl);
		System.out.println(booking.getUser().getEmail());

		// Gửi email với đường dẫn URL của hình ảnh đính kèm
		BookingConfirmEmailContext bookingConfirmEmailContext = new BookingConfirmEmailContext();

		User user = booking.getUser();
		bookingConfirmEmailContext.init(booking);
		bookingConfirmEmailContext.setQrCodeData(qrCodeUrl);
		bookingConfirmEmailContext.setBooking(booking);
		try {
			emailService.sendMail(bookingConfirmEmailContext);
//			emailService.sendEmailWithBooking(booking.getUser().getEmail(), "Order Confirmation",
//					"Thanks for your recent order", qrCodeUrl);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Lỗi");
		}

	}

	@Autowired
	StatusBookingService statusBookingService;

	@GetMapping("/kiemtrave/{bookingid}")
	public ModelAndView updatekiemtrave(@PathVariable("bookingid") Integer bookingid, Model model,
			Principal principal) {
		String userRole = getUserRole(principal);

		// Kiểm tra quyền nếu là Admin -> chuyển trạng thái
		if ("Administrator".equals(userRole)) {
			Booking booking = bookingService.findById(bookingid);
			if (booking.getStatusbooking().getStatusbookingid() == 5) {
				System.out.println("Vé đã được sử dụng");
				// Trả về giao diện Thymeleaf khi vé đã được sử dụng
				ModelAndView mav = new ModelAndView("mytiecketuse");
				// Thêm dữ liệu vào model nếu cần
				model.addAttribute("bookinguse", booking);
				mav.addObject("message", "Vé đã được sử dụng");
				return mav;
			} else {
				StatusBooking statusBooking = statusBookingService.findById(5);
				booking.setStatusbooking(statusBooking);
				Booking updatedBooking = bookingService.update(booking);
				System.out.println("Đã xác nhận thành công");
				// Trả về giao diện Thymeleaf khi xác nhận thành công
				ModelAndView mav = new ModelAndView("mytiecketuse");
				// Thêm dữ liệu vào model nếu cần
				model.addAttribute("bookinguse", booking);
				mav.addObject("message", "Đã xác nhận thành công");
				return mav;
			}
		} else {
			Booking booking = bookingService.findById(bookingid);
			ModelAndView mav = new ModelAndView("mytiecketseen");
			mav.addObject("message", "Bạn không có quyền xác nhận vé.");
			model.addAttribute("bookinguse", booking);

			return mav;
		}
	}

	// Lấy quyền user
	private String getUserRole(Principal principal) {
		User user = userService.findByEmail(principal.getName());

		if (user != null) {
			List<UserRole> userRoles = user.getUserRole();

			if (userRoles != null && !userRoles.isEmpty()) {
				// role
				Role role = userRoles.get(0).getRole();

				if (role != null) {
					return role.getName();
				}
			}
		}

		return "USER";
	}

}
