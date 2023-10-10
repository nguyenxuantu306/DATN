package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.Payment;

public interface PaymentDAO extends JpaRepository<Payment, Integer>{

}
