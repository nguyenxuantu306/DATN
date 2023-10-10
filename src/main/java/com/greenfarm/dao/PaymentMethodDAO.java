package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.Payment;

public interface PaymentMethodDAO extends JpaRepository<Payment, Integer>{

}
