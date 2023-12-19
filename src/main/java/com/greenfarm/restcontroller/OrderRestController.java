package com.greenfarm.restcontroller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.greenfarm.dto.OrderDTO;
import com.greenfarm.entity.CategorySalesByDate;
import com.greenfarm.entity.FindReportYear;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.Report7day;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.ReportYear;
import com.greenfarm.service.OrderService;
import com.greenfarm.utils.Log;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/orders")
public class OrderRestController {
	@Autowired
	OrderService orderService;

	@Autowired
	ModelMapper modelMapper;

	// Lấy danh sách đơn hàng
	@GetMapping()
	public ResponseEntity<List<OrderDTO>> getList() {
		try {
			Log.info("Nhận yêu cầu lấy danh sách đơn hàng");

			List<Order> orders = orderService.findAll();

			// Sử dụng ModelMapper để ánh xạ từ danh sách Order sang danh sách OrderDTO
			ModelMapper modelMapper = new ModelMapper();
			List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
					.collect(Collectors.toList());

			Log.info("Trả về danh sách đơn hàng thành công.");
			// Trả về danh sách OrderDTO bằng ResponseEntity với mã trạng thái 200 OK
			return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
		} catch (Exception e) {
			Log.error("Đã xảy ra lỗi khi lấy danh sách đơn hàng", e);
			// Trả về ResponseEntity với mã trạng thái 500 INTERNAL SERVER ERROR nếu có lỗi
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/search")
	public ResponseEntity<String> searchOrdersByDate(
			@RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime startDateTime,
			@RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime endDateTime,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		List<Order> orders = orderService.findByOrderdateBetween(startDateTime, endDateTime, page, size);

		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		try {
			String json = objectMapper.writeValueAsString(orderDTOs);
			return ResponseEntity.ok(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping()
	public Order create(@RequestBody JsonNode orderData) {
		return orderService.create(orderData);
	}

	// Cập nhật một đơn hàng
	@PutMapping("{id}")
	public ResponseEntity<OrderDTO> update(@PathVariable("id") Integer id, @RequestBody Order order) {
		try {
			Log.info("Nhận yêu cầu cập nhật đơn hàng với ID: {}", id);

			// Thực hiện cập nhật trong service
			Order updatedOrder = orderService.update(order);

			if (updatedOrder == null) {
				Log.warn("Không tìm thấy đơn hàng với ID {}. Không thể cập nhật.", id);
				return ResponseEntity.notFound().build();
			}

			// Sử dụng ModelMapper để ánh xạ từ Order đã cập nhật thành OrderDTO
			ModelMapper modelMapper = new ModelMapper();
			OrderDTO orderDTO = modelMapper.map(updatedOrder, OrderDTO.class);

			Log.info("Đơn hàng với ID {} đã được cập nhật thành công.", id);
			return new ResponseEntity<>(orderDTO, HttpStatus.OK);
		} catch (Exception e) {
			Log.error("Đã xảy ra lỗi khi cập nhật đơn hàng với ID: {}", id, e);
			// Trả về ResponseEntity với mã trạng thái 500 INTERNAL SERVER ERROR nếu có lỗi
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/byStatusName")
	public ResponseEntity<List<OrderDTO>> getOrdersByStatusName(@RequestParam("statusName") String statusName) {
		List<Order> orders = orderService.getOrdersByStatusName(statusName);

		// Sử dụng ModelMapper để ánh xạ từ Order sang OrderDTO
		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());

		return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
	}

	@GetMapping("/filter")
	public ResponseEntity<List<OrderDTO>> filterOrdersByNgayTao(
			@RequestParam("ngayTao") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDateTime ngayTao) {
		List<Order> filteredOrders = orderService.filterOrdersByNgayTao(ngayTao);

		// Sử dụng ModelMapper để ánh xạ từ Order sang OrderDTO
		List<OrderDTO> filteredOrderDTOs = filteredOrders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());

		return ResponseEntity.ok(filteredOrderDTOs);
	}

	// Thống kê số lượng trạng thái
	@GetMapping("/slstatus")
	public ResponseEntity<List<ReportRevenue>> slstatus() {
		return new ResponseEntity<>(orderService.slstatus(), HttpStatus.OK);
	}

	// Hủy một đơn hàng
	@PutMapping("/cancel/{orderid}")
	public ResponseEntity<String> cancelOrder(@PathVariable("orderid") Integer orderid) {
		try {
			Log.info("Nhận yêu cầu hủy đơn hàng với ID: {}", orderid);

			// Thực hiện các thao tác hủy đơn hàng trong service
			orderService.cancelOrder(orderid);

			Log.info("Đã hủy đơn hàng với ID {} thành công.", orderid);
			return new ResponseEntity<>("Đã hủy đơn hàng thành công", HttpStatus.OK);
		} catch (Exception e) {
			Log.error("Lỗi khi hủy đơn hàng với ID: {}", orderid, e);
			return new ResponseEntity<>("Lỗi khi hủy đơn hàng: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/year-revenue")
	public ResponseEntity<List<ReportYear>> getyearRevenue() {
		List<ReportYear> yearRevenue = orderService.getYearRevenue();
		return new ResponseEntity<>(yearRevenue, HttpStatus.OK);
	}

	@GetMapping("/findyearrevenue/{year}")
	public List<FindReportYear> getYearlyRevenue(@PathVariable Integer year) {
		return orderService.findYearlyRevenue(year);
	}

	@GetMapping("/getCategorySalesByDate")
	public ResponseEntity<List<CategorySalesByDate>> getCategorySalesByDate(
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

		List<CategorySalesByDate> result = orderService.getCategorySalesByDate(date);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/last7days")
	public List<Report7day> getRevenueLast7Days() {
		return orderService.getRevenueLast7Days();
	}
}
