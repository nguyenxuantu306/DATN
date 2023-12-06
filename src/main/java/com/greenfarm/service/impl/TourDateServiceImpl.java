package com.greenfarm.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.TourDateDAO;
import com.greenfarm.dto.TourDTO;
import com.greenfarm.entity.Category;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.TourDate;
import com.greenfarm.service.TourConditionService;
import com.greenfarm.service.TourDateService;

@Service
public class TourDateServiceImpl implements TourDateService {

	@Autowired
	TourDateDAO dao;
	
	@Override
	public List<TourDate> findAll() {
		return dao.findAll();
	}

	
	@Override
	public TourDate create(TourDate tourdate) {
		return dao.save(tourdate);
	}
	
	

	@Override
	public TourDate update(TourDate tourdate) {
		return dao.save(tourdate);
	}

	@Override
	public void deleteTourDateById(Integer tourdateid) {
			dao.deleteById(tourdateid);
		
	}

	@Override
	public TourDate findById(Integer tourdateid) {
		return dao.findById(tourdateid).get();
	}



	@Override
	public TourDate findByTourAndTourdates(Tour tour, Date tourdates) {
		return dao.findByTourAndTourdates(tour, tourdates).get(0);
	}

	
}
