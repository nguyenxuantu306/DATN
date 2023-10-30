package com.greenfarm.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.greenfarm.dao.TourConditionDAO;
import com.greenfarm.dto.TourConditionDTO;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.TourCondition;
import com.greenfarm.service.TourConditionService;

@Service
public class TourConditionServiceImpl implements TourConditionService {
	@Autowired
	TourConditionDAO dao;

	@Override
	public TourCondition create(TourCondition tourCondition) {
		return dao.save(tourCondition);
	}

	@Override
	public Optional<TourCondition> findByTourId(Integer tourid) {
		return dao.findById(tourid);
	}

	@Override
	public TourCondition update(TourCondition existingTourCondition) {
		return dao.save(existingTourCondition);
	}

}
