package com.greenfarm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.PricingDAO;
import com.greenfarm.entity.Pricing;
import com.greenfarm.service.PricingService;

@Service
public class PricingServiceImpl implements PricingService {
	@Autowired
	PricingDAO dao;

	
	public Pricing create(Pricing pricings) {
		return dao.save(pricings);
	}

	@Override
	public Pricing findByTourId(Integer tourid) {
		return dao.findById(tourid).get();
	}

	@Override
	public Pricing update(Pricing existingPricing) {
		return dao.save(existingPricing);
	}

}
