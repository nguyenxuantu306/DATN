package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.greenfarm.entity.Category;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.TourDate;
import com.greenfarm.entity.TourdateByDate;
import com.greenfarm.entity.Tour;

import java.time.LocalDate;
import java.util.Date;


public interface TourDateDAO extends JpaRepository<TourDate, Integer> {
	
	
	List<TourDate> findByTourAndTourdates(Tour tour, Date tourdates);
	
	
	@Query("SELECT p FROM TourDate p WHERE p.tour.tourname LIKE %:keyword% OR p.tour.departureday LIKE %:keyword%")
	List<TourDate> findByKeywordOrTourName(@Param("keyword") String keyword);



	@Query("SELECT p FROM TourDate p WHERE DATE(p.tourdates) = :date")
	List<TourDate> findByTourdates(@Param("date") Date date);


	@Query("SELECT NEW TourdateByDate(" +
		       "COUNT(DISTINCT b.Bookingid), " +
		       "SUM(b.Adultticketnumber), " +
		       "SUM(b.Childticketnumber)) " +
		       "FROM TourDateBooking tdb " +
		       "JOIN tdb.booking b " +
		       "JOIN b.statusbooking sb " +
		       "WHERE DATE(tdb.tourdate.tourdates) = :date " +
		       "AND sb.name = 'Đặt tour thành công'")
	List<TourdateByDate> getTourByDate(@Param("date") Date date);









}
