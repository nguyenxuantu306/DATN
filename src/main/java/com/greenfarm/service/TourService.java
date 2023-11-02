package com.greenfarm.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.greenfarm.dto.TourDTO;
import com.greenfarm.entity.Top10;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.TourCondition;
import com.greenfarm.entity.TourOverview;

public interface TourService {

	List<Tour> findAll();

	Tour findById(Integer tourid);

	//Thêm tour
	Tour create(Tour tour);

	Tour update(Tour tour);

	void delete(Integer tourid);

	List<TourDTO> findToursByAdultPrice(Float minPrice, Float maxPrice);

	List<TourDTO> findToursByTourname(String searchTerm);

}
