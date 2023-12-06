package com.greenfarm.service;

import java.util.List;

import com.greenfarm.entity.User;
import com.greenfarm.entity.VoucherUser;

public interface VoucherUserService {

	List<VoucherUser> findByUser(User user);

	List<VoucherUser> findAll();
	
	VoucherUser findById(Integer voucheruserId);

	VoucherUser create(VoucherUser voucheruser);

	VoucherUser update(VoucherUser voucheruser);

	void delete(Integer voucheruserid);
}
