package com.greenfarm.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greenfarm.entity.Order;
import com.greenfarm.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	ModelMapper modelMapper;

//	@RequestMapping("/order/list")
//	public String list(Model model, HttpServletRequest request) {
//		String email = request.getRemoteUser();
//		List<Order> orders = orderService.findByEfindByIdAccountmail(email);
//		// model.addAttribute("orders", orderService.findByEfindByIdAccountmail(email));
//		// Sắp xếp danh sách orders theo ngày mua gần nhất (orderDate giảm dần)
//		Collections.sort(orders, (o1, o2) -> o2.getOrderDateFormatted().compareTo(o1.getOrderDateFormatted()));
//
//		// Đặt danh sách đã sắp xếp vào model
//		model.addAttribute("sortedOrders", orders);
//		return "order/list";
//	}

	@RequestMapping("/order/list")
	public String list(Model model, HttpServletRequest request,
			@RequestParam(name = "statusFilter", required = false) String statusFilter) {
		String email = request.getRemoteUser();
		System.out.println("Status Filter: " + statusFilter);
		List<Order> orders = orderService.findByEfindByIdAccountmail(email);

		if (statusFilter != null && !statusFilter.isEmpty()) {
			orders = orders.stream().filter(order -> {
				System.out.println("Order Status: " + order.getStatusOrder().getName());
				return order.getStatusOrder().getName().equalsIgnoreCase(statusFilter);
			}).collect(Collectors.toList());
		}  else {
	        orders = orders.stream().filter(order -> order.getStatusOrder().getStatusorderid() == 1).collect(Collectors.toList());
	    }

		Collections.sort(orders, (o1, o2) -> o2.getOrderDateFormatted().compareTo(o1.getOrderDateFormatted()));

		model.addAttribute("sortedOrders", orders);
		return "order/list";
	}

	@RequestMapping("/order/detail/{orderid}")
	public String detail(@PathVariable("orderid") Integer orderid, Model model) {
		model.addAttribute("order", orderService.findById(orderid));
		return "order/detail";
	}

}
