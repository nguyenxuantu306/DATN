package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.greenfarm.entity.Booking;
import com.greenfarm.entity.Category;
import com.greenfarm.entity.Tour;

public interface TourDAO extends JpaRepository<Tour, Integer> {

	@Query("SELECT t FROM Tour t JOIN t.pricings p WHERE p.adultprice BETWEEN :minPrice AND :maxPrice AND t.isdeleted = false")
	Page<Tour> findByAdultpriceAndIsdeletedFalse(Float minPrice, Float maxPrice, Pageable pageable);

    Page<Tour> findByTournameContainingIgnoreCaseAndIsdeletedFalse(String searchTerm, Pageable pageable);
	
	List<Tour> findByTournameContainingIgnoreCase(String searchTerm);
	
	// tìm kiếm keywword
	@Query("SELECT t FROM Tour t WHERE LOWER(t.tourname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.departureday) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<Tour> findByKeyword(@Param("keyword") String keyword);
	
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

	Page<Tour> findAllByIsdeletedFalse(Pageable pageable);

	Page<Tour> findByDepartureday(String departureday, Pageable pageable);

}
