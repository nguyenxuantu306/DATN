package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.VoucherOrder;

public interface VoucherOrderDAO extends JpaRepository<VoucherOrder, Integer>{

}
