package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.OrderDetailDAO;
import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.Report;
import com.greenfarm.service.OrderDetailService;


@Service
public class OrderDetailServiceImpl implements OrderDetailService {
	@Autowired
	OrderDetailDAO dao;

	@Override
	public List<OrderDetail> findAll() {
		return dao.findAll();
	}

	@Override
	public List<Report> tongdoanhthu() {
		return dao.reportTheoLuotMuaHang();
	}

}
