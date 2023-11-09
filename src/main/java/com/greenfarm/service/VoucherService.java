package com.greenfarm.service;

import java.util.List;

import com.greenfarm.entity.Product;
import com.greenfarm.entity.Voucher;

public interface VoucherService {

	List<Voucher> findAll();

	Voucher findById(Integer voucherid);

	Voucher create(Voucher voucher);

	Voucher update(Voucher voucher);

	void delete(Integer voucherid);

}
