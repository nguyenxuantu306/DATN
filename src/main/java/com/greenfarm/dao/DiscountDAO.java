package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.Discount;

public interface DiscountDAO extends JpaRepository<Discount, Integer>{

}
