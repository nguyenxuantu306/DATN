package com.greenfarm.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.greenfarm.entity.Order;
import com.greenfarm.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class OrderController {

	@Autowired
	OrderService orderService;

	@RequestMapping("/order/list")
	public String list(Model model, HttpServletRequest request) {
		String email = request.getRemoteUser();
		model.addAttribute("orders", orderService.findByEfindByIdAccountmail(email));
		return "order/list";
	}


	@RequestMapping("/order/detail/{orderid}")
	public String detail(@PathVariable("orderid") Integer orderid, Model model) {
		model.addAttribute("order", orderService.findById(orderid));
		return "order/detail";
	}
	
	


}
