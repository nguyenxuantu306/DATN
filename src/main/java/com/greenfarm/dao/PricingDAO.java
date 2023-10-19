package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.Pricing;

public interface PricingDAO extends JpaRepository<Pricing, Integer>{

}
