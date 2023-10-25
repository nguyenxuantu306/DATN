package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.greenfarm.dao.StatusOrderDAO;
import com.greenfarm.entity.StatusOrder;
import com.greenfarm.service.StatusOrderService;

@Service
public class StatusOrderServiceImpl implements StatusOrderService {
	@Autowired
	StatusOrderDAO dao;

	@Override
	public List<StatusOrder> findAll() {
		return dao.findAll();
	}


}
