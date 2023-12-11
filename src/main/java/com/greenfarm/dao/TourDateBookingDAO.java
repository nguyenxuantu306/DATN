package com.greenfarm.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.greenfarm.entity.TourDateBooking;

public interface TourDateBookingDAO extends JpaRepository<TourDateBooking, Integer> {
	
	
	@Query("SELECT vu FROM TourDateBooking vu WHERE LOWER(vu.booking.tour.tourname) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<TourDateBooking> findByKeyword(@Param("keyword") String keyword);

	
	@Query("SELECT p FROM TourDateBooking p WHERE p.booking.tour.departureday = :departureday")
//	@Query("SELECT p FROM TourDateBooking p WHERE LOWER(p.tourdate.tour.departureday) LIKE LOWER(CONCAT('%', :departureday, '%'))")
		List<TourDateBooking> findByDepartureDay(@Param("departureday") String departureday);



	@Query("SELECT p FROM TourDateBooking p WHERE DATE(p.tourdate.tourdates) = :date")
	List<TourDateBooking> findByTourdates(@Param("date") Date date);


	List<TourDateBooking> findByTourdate(TourDate tourdate);


	List<TourDateBooking> findBytourdate(TourDate tourdate);


}
