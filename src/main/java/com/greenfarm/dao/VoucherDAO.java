package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.greenfarm.entity.Voucher;

@EnableJpaRepositories
public interface VoucherDAO extends JpaRepository<Voucher, Integer> {

	Voucher findByVoucherid(long parseLong);
}
