package com.greenfarm.service;

import java.util.Optional;

import com.greenfarm.entity.TourCondition;

public interface TourConditionService {

	TourCondition create(TourCondition tourCondition);

	Optional<TourCondition> findByTourId(Integer tourid);

	TourCondition update(TourCondition existingTourCondition);
}
