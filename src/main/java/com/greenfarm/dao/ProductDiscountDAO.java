package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.ProductImage;

public interface ProductDiscountDAO extends JpaRepository<ProductImage, Integer> {

}
