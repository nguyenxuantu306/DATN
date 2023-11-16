package com.greenfarm.service;

import java.util.List;
import java.util.Optional;

import com.greenfarm.entity.Product;
import com.greenfarm.entity.Voucher;
import com.greenfarm.entity.VoucherOrder;

public interface VoucherService {

	List<Voucher> findAll();

	Voucher findById(Integer voucherId);

	Voucher create(Voucher voucher);

	Voucher update(Voucher voucher);

	void delete(Integer voucherid);


	Voucher findByVoucherid(long parseLong);

}
