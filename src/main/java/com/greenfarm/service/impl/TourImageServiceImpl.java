package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.TourImageDAO;
import com.greenfarm.entity.TourImage;
import com.greenfarm.service.TourImageService;

@Service
public class TourImageServiceImpl implements TourImageService {
	@Autowired
	TourImageDAO dao;

	@Override
	public List<TourImage> findAll() {
		return dao.findAll();
	}
}
