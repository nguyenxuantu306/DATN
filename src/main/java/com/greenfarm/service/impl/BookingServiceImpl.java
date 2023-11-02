package com.greenfarm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.BookingDAO;
import com.greenfarm.entity.Top3;
import com.greenfarm.service.BookingService;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	BookingDAO dao;

	@Override
	public Page<Top3> getTop3Tour(Pageable pageable1) {
		return dao.getTop3Tour(pageable1);
	}

}
