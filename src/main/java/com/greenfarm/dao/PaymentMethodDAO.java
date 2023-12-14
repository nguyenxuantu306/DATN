package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.PaymentMethod;

public interface PaymentMethodDAO extends JpaRepository<PaymentMethod, Integer> {

}
