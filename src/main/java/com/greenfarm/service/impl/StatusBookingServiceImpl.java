package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.greenfarm.dao.StatusBookingDAO;
import com.greenfarm.entity.StatusBooking;
import com.greenfarm.service.StatusBookingService;

@Service
public class StatusBookingServiceImpl implements StatusBookingService{
	
	@Autowired
	StatusBookingDAO dao;

	@Override
	public List<StatusBooking> findAll() {
		return dao.findAll();
	}

	@Override
	public StatusBooking findById(int i) {
		return dao.findById(i).get();
	}
}
