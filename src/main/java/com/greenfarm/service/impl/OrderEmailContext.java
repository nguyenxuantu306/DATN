package com.greenfarm.service.impl;

import org.springframework.web.util.UriComponentsBuilder;

import com.greenfarm.entity.AbstractEmailContext;
import com.greenfarm.entity.User;

public class OrderEmailContext extends AbstractEmailContext {
	
	Float tong;
	
	@Override
	public <T> void init(T context) {
		// we can do any common configuration setup here
		// like setting up some base URL and context
		User customer = (User) context; // we pass the customer informati
		put("firstName", customer.getFirstname());
		setTemplateLocation("emails/email-order");
		setSubject("Bạn vừa đặt hàng thành công");
		setFrom("no-reply@javadevjournal.com");
		setTo(customer.getEmail());
		setContext(getContext());
	}
	
	

	
}
