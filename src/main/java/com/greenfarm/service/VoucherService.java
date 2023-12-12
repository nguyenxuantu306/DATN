package com.greenfarm.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.greenfarm.entity.Voucher;

public interface VoucherService {

	List<Voucher> findAll();

	Voucher findById(Integer voucherId);

	Voucher create(Voucher voucher);

	Voucher update(Voucher voucher);

	void delete(Integer voucherid);
	
	void save(Voucher voucher);
	

	List<Voucher> findByKeyword(String keyword);

	Voucher findByVoucherid(long parseLong);

	List<Voucher> findAllDeletedVouchers();

	Page<Voucher> findAllByIsdeletedFalse(Pageable pageable);
}
