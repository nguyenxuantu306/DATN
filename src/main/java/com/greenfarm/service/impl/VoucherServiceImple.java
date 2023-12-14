package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.VoucherDAO;
import com.greenfarm.entity.Voucher;
import com.greenfarm.service.VoucherService;

@Service
public class VoucherServiceImple implements VoucherService {
	@Autowired
	VoucherDAO dao;

	@Override
	public List<Voucher> findAll() {
		return dao.findAll();
	}

	@Override
	public Voucher findById(Integer voucherid) {
		return dao.findById(voucherid).get();
	}

	@Override
	public Voucher create(Voucher voucher) {
		return dao.save(voucher);
	}

	@Override
	public Voucher update(Voucher voucher) {
		return dao.save(voucher);
	}

	@Override
	public void delete(Integer voucherid) {
		dao.deleteById(voucherid);
	}

	@Override
	public Voucher findByVoucherid(long parseLong) {
		return dao.findByVoucherid(parseLong);
	}

}
