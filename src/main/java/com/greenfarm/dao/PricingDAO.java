package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.Pricing;

public interface PricingDAO extends JpaRepository<Pricing, Integer>{
	List<Pricing> findByAdultpriceBetween(Float minPrice, Float maxPrice);
}
