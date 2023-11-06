package com.greenfarm.restcontroller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.greenfarm.dto.OrderDTO;
import com.greenfarm.entity.FindReportYear;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.ReportYear;
import com.greenfarm.entity.RevenueTK;
import com.greenfarm.service.OrderDetailService;
import com.greenfarm.service.OrderService;
import com.greenfarm.service.ProductService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/orders")
public class OrderRestController {
	@Autowired
	OrderService orderService;
	
	 @Autowired
	 private ProductService productService;

	 @Autowired
	 private OrderDetailService orderDetailService;

	@Autowired
	ModelMapper modelMapper;

	// @GetMapping()
	// public ResponseEntity<String> getAllOrders(
	// @RequestParam(defaultValue = "0") int page,
	// @RequestParam(defaultValue = "10") int size) {
	// List<Order> orders = orderService.getAllOrders(page, size);
	// List<OrderDTO> orderDTOs = orders.stream()
	// .map(order -> modelMapper.map(order, OrderDTO.class))
	// .collect(Collectors.toList());
	//
	// ObjectMapper objectMapper = new ObjectMapper();
	// try {
	// String json = objectMapper.writeValueAsString(orderDTOs);
	// return ResponseEntity.ok(json);
	// } catch (JsonProcessingException e) {
	// // Xử lý lỗi nếu chuyển đổi sang JSON không thành công
	// e.printStackTrace();
	// return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	// }
	// }

	@GetMapping()
	public ResponseEntity<List<OrderDTO>> getList() {
		List<Order> orders = orderService.findAll();

		// Sử dụng ModelMapper để ánh xạ từ danh sách Product sang danh sách ProductDTO
		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());

		// Trả về danh sách ProductDTO bằng ResponseEntity với mã trạng thái 200 OK
		return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
	}

	public ResponseEntity<String> getAllOrders(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		List<Order> orders = orderService.getAllOrders(page, size);
		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String json = objectMapper.writeValueAsString(orderDTOs);
			return ResponseEntity.ok(json);
		} catch (JsonProcessingException e) {
			// Xử lý lỗi nếu chuyển đổi sang JSON không thành công
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// @PostMapping()
	// public Order create(@RequestBody JsonNode orderData) {
	// ObjectMapper objectMapper = new ObjectMapper();
	// OrderDTO orderDTO = objectMapper.convertValue(orderData, OrderDTO.class);
	//
	// // Gọi service để tạo đối tượng Order từ OrderDTO
	// Order order = orderService.create(orderDTO);
	//
	// return order;
	// }
	// @GetMapping
	// public List<Order> getAll() {
	// return orderService.findAll();
	// }

	// @GetMapping()
	// public List<OrderDTO> getAll() {
	// return orderService.findAll();
	// }

	@PostMapping()
	public Order create(@RequestBody JsonNode orderData) {
		return orderService.create(orderData);
	}

	@PutMapping("{id}")
	public ResponseEntity<OrderDTO> update(@PathVariable("id") Integer id,
			@RequestBody Order order) {
		Order updatedOrder = orderService.update(order);

		if (updatedOrder == null) {
			return ResponseEntity.notFound().build();
		}
		OrderDTO orderDTO = modelMapper.map(updatedOrder, OrderDTO.class);
		return new ResponseEntity<>(orderDTO, HttpStatus.OK);
	}

	@GetMapping("/byStatusName")
	public ResponseEntity<List<OrderDTO>> getOrdersByStatusName(@RequestParam("statusName") String statusName) {
		List<Order> orders = orderService.getOrdersByStatusName(statusName);

		// Sử dụng ModelMapper để ánh xạ từ Order sang OrderDTO
		List<OrderDTO> orderDTOs = orders.stream()
				.map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());

		return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
	}

	@GetMapping("/filter")
	public ResponseEntity<List<OrderDTO>> filterOrdersByNgayTao(
			@RequestParam("ngayTao") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDateTime ngayTao) {
		List<Order> filteredOrders = orderService.filterOrdersByNgayTao(ngayTao);

		// Sử dụng ModelMapper để ánh xạ từ Order sang OrderDTO
		List<OrderDTO> filteredOrderDTOs = filteredOrders.stream()
				.map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());

		return ResponseEntity.ok(filteredOrderDTOs);
	}

	// Thống kê số lượng trạng thái
	@GetMapping("/slstatus")
	public ResponseEntity<List<ReportRevenue>> slstatus() {
		return new ResponseEntity<>(orderService.slstatus(), HttpStatus.OK);
	}

	@PutMapping("/cancel/{orderid}")
	public ResponseEntity<String> cancelOrder(@PathVariable("orderid") Integer orderid) {
		// Thực hiện các thao tác hủy đơn hàng
		try {
			orderService.cancelOrder(orderid);
			return new ResponseEntity<>("Đã hủy đơn hàng thành công", HttpStatus.OK);
		} catch (Exception e) {
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
}
