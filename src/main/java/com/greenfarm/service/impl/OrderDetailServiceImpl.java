package com.greenfarm.service.impl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.OrderDetailDAO;
import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.Top10;
import com.greenfarm.service.OrderDetailService;


@Service
public class OrderDetailServiceImpl implements OrderDetailService {
	@Autowired
	OrderDetailDAO dao;

	@Override
	public List<OrderDetail> findAll() {
		// TODO Auto-generated method stub
		return dao.findAll();
	}

	@Override
	public List<Report> tongdoanhthu() {
		// TODO Auto-generated method stub
		return dao.reportTheoLuotMuaHang();
	}

	@Override
    public Page<Top10> getTop10(Pageable pageable) {
        return dao.getTop10(pageable);
    }

   
}