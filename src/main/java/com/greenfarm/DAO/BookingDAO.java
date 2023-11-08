package com.greenfarm.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.greenfarm.entity.Booking;
import com.greenfarm.entity.FindReportYear;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.ReportYear;
import com.greenfarm.entity.Top3;

public interface BookingDAO extends JpaRepository<Booking, Integer> {

	@Query("SELECT new Top3(o.tour, SUM(o.Adultticketnumber + o.Childticketnumber))"
			+ " FROM Booking o GROUP BY o.tour ORDER BY SUM(o.Adultticketnumber + o.Childticketnumber)"
			+ " DESC")
	Page<Top3> getTop3Tour(Pageable pageable1);

	@Query("SELECT o FROM Booking o")
	List<Booking> findAll(int offset, int limit);

	@Query("SELECT o FROM Booking o WHERE o.bookingdate  BETWEEN :startDateTime AND :endDateTime")
	List<Booking> findByBookingdateBetween(
			
			@Param("startDateTime") LocalDateTime startDateTime, 
			
			@Param("endDateTime") LocalDateTime endDateTime, Pageable pageable
			
			);

	@Query("SELECT new ReportRevenue(o.statusbooking.name, count(o)) FROM Booking o GROUP BY o.statusbooking.name")
	List<ReportRevenue> countBookingsByStatus();

	
	@Query("SELECT NEW ReportYear(YEAR(o.bookingdate), sum(o.Totalprice)) " + "FROM Booking o "
			+ "GROUP BY YEAR(o.bookingdate)" + "ORDER BY YEAR(o.bookingdate)")
	List<ReportYear> getbookingYearRevenue();

	
	@Query("SELECT NEW com.greenfarm.entity.FindReportYear(m.month, coalesce(SUM(o.Totalprice), 0)) " +
		       "FROM (SELECT 1 AS month UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 " +
		       "      UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12) AS m " +
		       "LEFT JOIN Booking o ON m.month = MONTH(o.bookingdate) AND YEAR(o.bookingdate) = :year " +		
		       "GROUP BY m.month " +
		       "ORDER BY m.month")
	List<FindReportYear> findBookingYearlyRevenue(@Param("year") Integer year);
	
	@Query("SELECT o FROM Booking o WHERE o.user.email =?1")
	List<Booking> findByEfindByIdAccountmail(String email);
}
