package com.greenfarm.restcontroller;

import java.util.List;

import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.greenfarm.entity.Order;
import com.greenfarm.service.OrderService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/orders")
public class OrderRestController {
	@Autowired
	OrderService orderService;

	@Autowired
	ModelMapper modelMapper;

	@GetMapping()
	public ResponseEntity<String> getAllOrders(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {
	    List<Order> orders = orderService.getAllOrders(page, size);
	    List<OrderDTO> orderDTOs = orders.stream()
	            .map(order -> modelMapper.map(order, OrderDTO.class))
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

//	@PostMapping()
//	public Order create(@RequestBody JsonNode orderData) {
//	    ObjectMapper objectMapper = new ObjectMapper();
//	    OrderDTO orderDTO = objectMapper.convertValue(orderData, OrderDTO.class);
//	    
//	    // Gọi service để tạo đối tượng Order từ OrderDTO
//	    Order order = orderService.create(orderDTO);
//	    
//	    return order;
//	}
//	@GetMapping
//	public List<Order> getAll() {
//		return orderService.findAll();
//	}

//	@GetMapping()
//	public List<OrderDTO> getAll() {
//		return orderService.findAll();
//	}

	@PostMapping()
	public Order create(@RequestBody JsonNode orderData) {
		return orderService.create(orderData);
	}
	
	@PutMapping("{id}")
	public Order update(@PathVariable("id") Integer id, @RequestBody Order order) {
		return orderService.update(order);
	}
}