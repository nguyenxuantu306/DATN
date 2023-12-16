package com.greenfarm.service.impl;


import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.greenfarm.entity.AbstractEmailContext;
import com.greenfarm.entity.Booking;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.User;
import com.greenfarm.service.OrderDetailService;

public class OrderConfirmEmailContext extends AbstractEmailContext {
	private Order order;
	
	@Autowired
	OrderDetailService  orderDetailService;

	@Override
	public <T> void init(T context) {
		// we can do any common configuration setup here
		// like setting up some base URL and context
		Order order = (Order) context;
		System.out.println(order.getOrderid());
		User customer = order.getUser();
		put("firstName", customer.getFirstname());
		setTemplateLocation("emails/emailsendorder");
		setSubject("ConFirm Order");
		setFrom("no-reply@javadevjournal.com");
		setTo(customer.getEmail());
		
		put("Order", order);
		List<OrderDetail> list = order.getOrderDetail();
		put("list", list);
	}

	
//	public void buildVerificationUrl(final String baseURL, final String token) {
//		final String url = UriComponentsBuilder.fromHttpUrl(baseURL).path("/register/verify").queryParam("token", token)
//				.toUriString();
//		put("verificationURL", url);
//	}

}
