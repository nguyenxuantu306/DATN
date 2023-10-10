package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.OrderDetail;

public interface OrderDetailDAO extends JpaRepository<OrderDetail, Integer>{

}
