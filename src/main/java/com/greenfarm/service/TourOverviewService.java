package com.greenfarm.service;

import java.util.Optional;

import com.greenfarm.entity.TourOverview;

public interface TourOverviewService {

	TourOverview create(TourOverview tourOverview);

	Optional<TourOverview> findByTourId(Integer tourid);

	TourOverview update(TourOverview existingTourOverview);

}
