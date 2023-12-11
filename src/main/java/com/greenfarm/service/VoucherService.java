package com.greenfarm.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.greenfarm.entity.Voucher;

public interface VoucherService {

	List<Voucher> findAll();

	List<Voucher> findAllDeletedVouchers();

	Voucher findById(Integer voucherId);

	Voucher create(Voucher voucher);

	Voucher update(Voucher voucher);

	void delete(Integer voucherid);

	void save(Voucher voucher);

	Voucher findByVoucherId(long parseLong);

	Page<Voucher> findAllByIsdeletedFalse(Pageable pageable);

	Voucher findByVoucherid(long parseLong);
}
