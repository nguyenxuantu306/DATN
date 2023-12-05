package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.greenfarm.dao.VoucherUserDAO;
import com.greenfarm.entity.User;
import com.greenfarm.entity.VoucherUser;
import com.greenfarm.service.VoucherUserService;

@Service
public class VoucherUserServiceImpl implements VoucherUserService{
	@Autowired
	VoucherUserDAO dao;

	@Override
	public List<VoucherUser> findByUser(User user) {
		return dao.findByUser(user);
	}
}
