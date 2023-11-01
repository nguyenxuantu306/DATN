package com.greenfarm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.PricingDAO;
import com.greenfarm.dao.TourConditionDAO;
import com.greenfarm.dao.TourDAO;
import com.greenfarm.dao.TourOverviewDAO;
import com.greenfarm.dto.TourDTO;
import com.greenfarm.entity.Pricing;
import com.greenfarm.entity.Tour;
import com.greenfarm.service.TourService;

@Service
public class TourServiceImpl implements TourService {
	
	@Autowired
	PricingDAO pricingdao;

	@Autowired
	TourDAO dao;

	@Autowired
	TourOverviewDAO overdao;

	@Autowired
	TourConditionDAO conditiondao;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public List<Tour> findAll() {
		return dao.findAll();
	}

	@Override
	public Tour findById(Integer tourid) {
		return dao.findById(tourid).get();
	}

	
	@Override
	public Tour create(Tour tour) {
		return dao.save(tour);
	}

	@Override
	public Tour update(Tour tour) {
		return dao.save(tour);
	}

	@Override
	public void delete(Integer tourid) {
		dao.deleteById(tourid);
	}

	public List<TourDTO> findToursByAdultPrice(Float minPrice, Float maxPrice) {
		List<Pricing> pricings = pricingdao.findByAdultpriceBetween(minPrice, maxPrice);

		List<TourDTO> tourDTOs = new ArrayList<>();
		for (Pricing pricing : pricings) {
			Tour tour = pricing.getTour();
			TourDTO tourDTO = new TourDTO();
			tourDTO.setTourid(tour.getTourid());
			tourDTO.setTourname(tour.getTourname());
			tourDTO.setPricings(pricing.getTour().getPricings());
			tourDTO.setImage(tour.getImage());
			// Thực hiện mapping các thuộc tính khác của TourDTO (nếu cần)
			tourDTOs.add(tourDTO);
		}

		return tourDTOs;
	}

}
