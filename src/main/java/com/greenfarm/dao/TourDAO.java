package com.greenfarm.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenfarm.entity.Tour;

public interface TourDAO extends JpaRepository<Tour, Integer> {

	@Query("SELECT t FROM Tour t JOIN t.pricings p WHERE p.adultprice BETWEEN :minPrice AND :maxPrice")
	Page<Tour> findByAdultprice(Float minPrice, Float maxPrice, Pageable pageable);

    Page<Tour> findByTournameContainingIgnoreCase(String searchTerm, Pageable pageable);
	
	List<Tour> findByTournameContainingIgnoreCase(String searchTerm);

}
