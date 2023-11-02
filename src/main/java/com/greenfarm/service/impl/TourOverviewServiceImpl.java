package com.greenfarm.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.TourOverviewDAO;
import com.greenfarm.entity.TourOverview;
import com.greenfarm.service.TourOverviewService;

@Service
public class TourOverviewServiceImpl implements TourOverviewService{
	@Autowired
	TourOverviewDAO dao;
	
	@Override
	public TourOverview create(TourOverview tourOverview) {
		return dao.save(tourOverview);
	}

	@Override
	public Optional<TourOverview> findByTourId(Integer tourid) {
		return dao.findById(tourid);
	}

	@Override
	public TourOverview update(TourOverview existingTourOverview) {
		return dao.save(existingTourOverview);
	}

}
