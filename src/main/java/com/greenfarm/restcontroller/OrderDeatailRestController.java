package com.greenfarm.restcontroller;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.Report;
import com.greenfarm.service.OrderDetailService;


@CrossOrigin("*")
@RestController
@RequestMapping("/rest/orderdetails")
public class OrderDeatailRestController {

	@Autowired
	OrderDetailService orderDetailService;

	@GetMapping
	public List<OrderDetail> getAll() {
		return orderDetailService.findAll();
	}
		
	@GetMapping("/thongke")
	public ResponseEntity<List<Report>>  tongdoanhthu(){	
		return new ResponseEntity<>(orderDetailService.tongdoanhthu(), HttpStatus.OK);
		
	}
	
}
