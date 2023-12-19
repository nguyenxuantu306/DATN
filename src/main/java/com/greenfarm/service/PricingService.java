package com.greenfarm.service;

import com.greenfarm.entity.Pricing;

public interface PricingService {

	Pricing create(Pricing pricings);

	Pricing findByTourId(Integer tourid);

	Pricing update(Pricing existingPricing);

}
