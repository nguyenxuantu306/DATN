package com.greenfarm.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.greenfarm.entity.Booking;
import com.greenfarm.entity.Tour;

public interface TourDAO extends JpaRepository<Tour, Integer> {

	@Query("SELECT t FROM Tour t JOIN t.pricings p WHERE p.adultprice BETWEEN :minPrice AND :maxPrice")
	Page<Tour> findByAdultprice(Float minPrice, Float maxPrice, Pageable pageable);

    Page<Tour> findByTournameContainingIgnoreCase(String searchTerm, Pageable pageable);
	
	List<Tour> findByTournameContainingIgnoreCase(String searchTerm);
	
	@Query("SELECT p FROM Tour p WHERE p.isdeleted = true")
	List<Booking> findAllDeletedBooking();

	@Query("SELECT p FROM Tour p WHERE p.tourid = :id")
	Tour findTourById(@Param("id") Integer id);
	
	default void deleteByIsDeleted(Integer id) {
		Tour tour = findTourById(id);
		if (tour != null) {
			tour.setIsDeleted(true);
			save(tour);
		}
	}

	List<Tour> findAllByIsdeletedFalse();

	List<Tour> findAllByIsdeletedTrue();

	@Modifying
	@Query("UPDATE Tour b SET b.isdeleted = true WHERE b.tourid = :id")
	void deleteTourById(@Param("id") Integer id);

}
