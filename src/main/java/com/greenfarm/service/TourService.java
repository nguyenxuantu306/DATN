package com.greenfarm.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.greenfarm.dto.TourDTO;
import com.greenfarm.entity.Category;
import com.greenfarm.entity.Tour;

public interface TourService {

	List<Tour> findAll();

	Tour findById(Integer tourid);

	List<Tour> findByKeyword(String keyword);
	
	// Thêm tour
	Tour create(Tour tour);

	Tour update(Tour tour);

	List<TourDTO> findToursByAdultPrice(Float minPrice, Float maxPrice);

	List<TourDTO> findToursByTourname(String searchTerm);

	Tour save(Tour tour);

	// Phương thức sinh mã TourId sử dụng UUID.randomUUID()
	public static String generateTourId() {
		return UUID.randomUUID().toString();
	}

	Page<TourDTO> findToursByAdultPriceWithPagination(Float minPrice, Float maxPrice, Pageable pageable);

	Page<TourDTO> findToursByTournameWithPagination(String searchTerm, Pageable pageable);

	Page<Tour> findAllWithPagination(Pageable pageable);

	void deleteTourById(Integer tourid);

	List<Tour> findAllDeletedTour();
}
