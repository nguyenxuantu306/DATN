package com.greenfarm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.TourDAO;
import com.greenfarm.entity.Top10;
import com.greenfarm.entity.Tour;
import com.greenfarm.service.TourService;

@Service
public class TourServiceImpl implements TourService {

	@Autowired
	TourDAO dao;
	
	
}
