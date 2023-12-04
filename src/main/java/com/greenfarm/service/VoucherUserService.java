package com.greenfarm.service;

import java.util.List;

import com.greenfarm.entity.User;
import com.greenfarm.entity.Voucher;
import com.greenfarm.entity.VoucherUser;

public interface VoucherUserService {

	List<VoucherUser> findByUser(User user);

	List<VoucherUser> findAll();
}
