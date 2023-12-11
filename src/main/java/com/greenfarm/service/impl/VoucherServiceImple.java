package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
		return dao.findAllByIsdeletedFalse();
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
		dao.deleteByIsDeleted(voucherid);
	}

	@Override
	public void save(Voucher voucher) {
		dao.save(voucher);

	}

	@Override
	public Voucher findByVoucherId(long parseLong) {
		return dao.findByVoucherid(parseLong);
	}

	@Override
	public List<Voucher> findAllDeletedVouchers() {
		return dao.findAllByIsdeletedTrue();
	}

	@Override
	public Page<Voucher> findAllByIsdeletedFalse(Pageable pageable) {
		return dao.findAllByIsdeletedFalse(pageable);
	}

	@Override
	public Voucher findByVoucherid(long parseLong) {
		return dao.findByVoucherid(parseLong);
	}

	@Override
	public List<Voucher> findByKeyword(String keyword) {
		return dao.findByKeyword(keyword);
	}

}
