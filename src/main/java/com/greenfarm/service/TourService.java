package com.greenfarm.service;

import java.util.List;

import com.greenfarm.entity.Tour;

public interface TourService {

	List<Tour> findAll();

	Tour findById(Integer tourid);

}
