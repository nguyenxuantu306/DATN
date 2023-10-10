package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.Order;

public interface OrderDAO extends JpaRepository<Order, Integer>{

}
