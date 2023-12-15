package com.greenfarm.service.impl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.greenfarm.dao.VoucherUserDAO;
import com.greenfarm.entity.User;
import com.greenfarm.entity.VoucherUser;
import com.greenfarm.service.VoucherUserService;

@Service
public class VoucherUserServiceImpl implements VoucherUserService {
	@Autowired
	VoucherUserDAO dao;

	@Override
	public List<VoucherUser> findByUser(User user) {
		return dao.findByUser(user);
	}
	
	@Override
	public VoucherUser findById(Integer voucheruserid) {
		return dao.findById(voucheruserid).get();
	}

	@Override
	public List<VoucherUser> findAll() {
		return dao.findAll();
	}

	@Override
	public VoucherUser create(VoucherUser voucheruser) {
		return dao.save(voucheruser);
	}

	@Override
	public VoucherUser update(VoucherUser voucheruser) {
		return dao.save(voucheruser);
	}

	@Override
	public void delete(Integer voucheruserid) {
		dao.deleteById(voucheruserid);
	}

	@Override
	public List<VoucherUser> findByKeyword(String keyword) {
		return dao.findByKeyword(keyword);
	}

	@Override
	public boolean existsByUserIdAndVoucherId(Integer userId, Integer voucherId) {
		// TODO Auto-generated method stub
		return dao.existsByUserUseridAndVoucherVoucherid(userId, voucherId);
	}


	
}
