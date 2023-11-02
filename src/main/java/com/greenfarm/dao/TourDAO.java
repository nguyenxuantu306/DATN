package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenfarm.entity.Tour;

public interface TourDAO extends JpaRepository<Tour, Integer> {

	List<Tour> findByTournameContainingIgnoreCase(String searchTerm);

}
