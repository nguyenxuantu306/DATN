package com.greenfarm.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenfarm.entity.Booking;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.Top3;

public interface BookingDAO extends JpaRepository<Booking, Integer> {

	@Query("SELECT new Top3(o.tour, SUM(o.Adultticketnumber + o.Childticketnumber))"
			+ " FROM Booking o GROUP BY o.tour ORDER BY SUM(o.Adultticketnumber + o.Childticketnumber)"
			+ " DESC")
	Page<Top3> getTop3Tour(Pageable pageable1);

	@Query("SELECT o FROM Booking o")
	List<Booking> findAll(int offset, int limit);

	@Query("SELECT o FROM Booking o WHERE o.bookingdate  BETWEEN :startDateTime AND :endDateTime")
	List<Booking> findByBookingdateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);
}
