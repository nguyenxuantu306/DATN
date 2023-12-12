package com.greenfarm.dao;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.greenfarm.entity.Category;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.TourDate;
import com.greenfarm.entity.TourDateBooking;
import com.greenfarm.entity.VoucherUser;

public interface TourDateBookingDAO extends JpaRepository<TourDateBooking, Integer> {
	
	
	@Query("SELECT vu FROM TourDateBooking vu WHERE LOWER(vu.booking.tour.tourname) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<TourDateBooking> findByKeyword(@Param("keyword") String keyword);

	
	@Query("SELECT tdb FROM TourDateBooking tdb " + "JOIN Booking b ON tdb.booking.Bookingid = b.Bookingid "
			+ "JOIN Tour t ON b.tour.tourid = t.tourid " + "WHERE t.departureday = :departureday")
		List<TourDateBooking> findByDepartureDay(@Param("departureday") String departureday);



	@Query("SELECT p FROM TourDateBooking p WHERE DATE(p.tourdate.tourdates) = :date")
	List<TourDateBooking> findByTourdates(@Param("date") Date date);


	List<TourDateBooking> findByTourdate(TourDate tourDate);


}
