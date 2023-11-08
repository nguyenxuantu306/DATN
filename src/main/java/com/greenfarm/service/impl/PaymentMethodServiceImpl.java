package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.greenfarm.dao.PaymentMethodDAO;
import com.greenfarm.entity.PaymentMethod;
import com.greenfarm.service.PaymentMethodService;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
	PaymentMethodDAO dao;

	@Override
	public List<PaymentMethod> findAll() {
		return dao.findAll();
	}
}
