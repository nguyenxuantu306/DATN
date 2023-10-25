package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.TourDAO;
import com.greenfarm.dto.TourDTO;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.Top10;
import com.greenfarm.entity.Tour;
import com.greenfarm.service.TourService;

@Service
public class TourServiceImpl implements TourService {

	@Autowired
	TourDAO dao;
	

	@Override
	public List<Tour> findAll() {
		return dao.findAll();
	}

	@Override
	public Tour findById(Integer tourid) {
		return dao.findById(tourid).get();
	}
	
	
}
