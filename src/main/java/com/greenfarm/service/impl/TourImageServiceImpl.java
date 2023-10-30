package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.TourImageDAO;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.TourImage;
import com.greenfarm.service.TourImageService;
import com.greenfarm.service.TourService;

@Service
public class TourImageServiceImpl implements TourImageService {
	@Autowired
	TourImageDAO dao;

	@Override
	public List<TourImage> findAll() {
		return dao.findAll();
	}

	@Override
	public void createAll(List<TourImage> tourImages) {
		 dao.saveAll(tourImages);
	}

//	@Override
//	public TourImage findByTourId(Integer tourid) {
//		return  dao.findByTourTourid(tourid);
//	}

	@Override
	public void create(List<TourImage> tourImages) {
		dao.saveAll(tourImages);
		
	}

	@Override
	public List<TourImage> findByTourTourid(Integer tourid) {
		// TODO Auto-generated method stub
		return dao.findByTourTourid(tourid);
	}

	@Override
	public void save(TourImage tourImage) {
		dao.save(tourImage);
		
	}
}
