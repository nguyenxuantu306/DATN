package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.Tour;


public interface TourDAO extends JpaRepository<Tour, Integer> {

	/*
	 * @Query("SELECT * FROM tour WHERE active = ?1") List<Tour>
	 * getToursByStatus(boolean activee);
	 */

//	@Query("SELECT p FROM Tours p WHERE p.Tours.TourID=?1")
//	List<Tour> findByTourId(String cid);
}
