package com.greenfarm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.greenfarm.dao.PricingDAO;
import com.greenfarm.dao.TourConditionDAO;
import com.greenfarm.dao.TourDAO;
import com.greenfarm.dao.TourOverviewDAO;
import com.greenfarm.dto.TourDTO;
import com.greenfarm.entity.Booking;
import com.greenfarm.entity.Pricing;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.TourImage;
import com.greenfarm.service.TourService;

import jakarta.persistence.EntityNotFoundException;

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
		return dao.findAllByIsdeletedFalse();
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

	@Override
	public List<TourDTO> findToursByTourname(String searchTerm) {
		List<Tour> tours = dao.findByTournameContainingIgnoreCase(searchTerm);
		return tours.stream().map(tour -> modelMapper.map(tour, TourDTO.class)).collect(Collectors.toList());
	}

	@Override
	public Tour save(Tour tour) {
		return dao.save(tour);
	}

	@Override
	public Page<TourDTO> findToursByAdultPriceWithPagination(Float minPrice, Float maxPrice, Pageable pageable) {
	    Page<Tour> tourPage = dao.findByAdultpriceAndIsdeletedFalse(minPrice, maxPrice, pageable);

	    List<TourDTO> tourDTOs = tourPage.getContent().stream()
	                                   .map(tour -> convertToDTOUsingModelMapper(tour))
	                                   .collect(Collectors.toList());

	    return new PageImpl<>(tourDTOs, pageable, tourPage.getTotalElements());
	}
	
	private TourDTO convertToDTOUsingModelMapper(Tour tour) {
	    return modelMapper.map(tour, TourDTO.class);
	}

	@Override
	public Page<TourDTO> findToursByTournameWithPagination(String searchTerm, Pageable pageable) {
	    Page<Tour> tourPage = dao.findByTournameContainingIgnoreCaseAndIsdeletedFalse(searchTerm, pageable);

	    List<TourDTO> tourDTOs = tourPage.getContent().stream()
	                                   .map(tour -> convertToDTOUsingModelMapper(tour))
	                                   .collect(Collectors.toList());

	    return new PageImpl<>(tourDTOs, pageable, tourPage.getTotalElements());
	}

	@Override
	public Page<Tour> findAllWithPagination(Pageable pageable) {
		return dao.findAllByIsdeletedFalse(pageable);
	}
	
	@Override
	public List<Tour> findAllDeletedTour() {
		return dao.findAllByIsdeletedTrue();
	}

	@Override
    @Transactional
    public void deleteTourById(Integer tourid) {
		dao.deleteTourById(tourid);
    }

}
