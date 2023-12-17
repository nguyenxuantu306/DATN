package com.greenfarm.service.impl;


import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.greenfarm.entity.AbstractEmailContext;
import com.greenfarm.entity.Booking;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.User;
import com.greenfarm.entity.VoucherOrder;
import com.greenfarm.service.OrderDetailService;

public class OrderConfirmEmailContext extends AbstractEmailContext {
	
	@Autowired
	OrderDetailService orderDetailService;
	
//	@Override
//	public <T> void init(T context) {
//		Order order = (Order) context;
//		System.out.println(order.getOrderid());
//		User customer = order.getUser();
//		put("firstName", customer.getFirstname());
//		put("order", order);
//		setTemplateLocation("emails/emailsendorder");
//		setSubject("ConFirm Order");
//		setFrom("no-reply@javadevjournal.com");
//		setTo(customer.getEmail());
//		
//	}
	
	public void init(Order orderItem, List<OrderDetail> orderDetailList,float total,List<String> formattedDiscounts, List<VoucherOrder> voucherLists, float discountedTotal) {
	    System.out.println(orderItem.getOrderid());
	    User customer = orderItem.getUser();
	    put("firstName", customer.getFirstname());
	    put("order", orderItem);
	    put("orderDetails", orderDetailList);
	    put("total", total);
	    put("formattedDiscounts",formattedDiscounts);
	    put("voucherLists", voucherLists);
	    put("totalDiscount", discountedTotal);
	    setTemplateLocation("emails/emailsendorder");
	    setSubject("Confirm Order");
	    setFrom("no-reply@javadevjournal.com");
	    setTo(customer.getEmail());
	}

	
	
//	public void buildVerificationUrl(final String baseURL, final String token) {
//		final String url = UriComponentsBuilder.fromHttpUrl(baseURL).path("/register/verify").queryParam("token", token)
//				.toUriString();
//		put("verificationURL", url);
//	}

}
