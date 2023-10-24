package com.greenfarm.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.greenfarm.dto.TourDTO;
import com.greenfarm.entity.Top10;
import com.greenfarm.entity.Tour;

public interface TourService {

	List<Tour> findAll();

	Tour findById(Integer tourid);
	
	
}
