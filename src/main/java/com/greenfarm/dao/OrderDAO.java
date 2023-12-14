package com.greenfarm.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import com.greenfarm.entity.CategorySalesByDate;
import com.greenfarm.entity.FindReportYear;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.Report7day;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.ReportYear;

public interface OrderDAO extends JpaRepository<Order, Integer> {

	@Query("SELECT o FROM Order o WHERE o.statusOrder.name = ?1")
	List<Order> findByStatusOrder_Name(String statusName);

	@Query("SELECT o FROM Order o WHERE o.orderdate  >= :startDay AND o.orderdate < :endDay")
	List<Order> findOrdersByDateRange(@Param("startDay") LocalDateTime startDay, @Param("endDay") LocalDateTime endDay);

	@Query("SELECT o FROM Order o WHERE o.orderdate  BETWEEN :startDateTime AND :endDateTime")
	List<Order> findByOrderdateBetween(@Param("startDateTime") LocalDateTime startDateTime,
			@Param("endDateTime") LocalDateTime endDateTime, Pageable pageable);

	@Query("SELECT o FROM Order o")
	List<Order> findAll(int offset, int limit);

	@Query("SELECT o FROM Order o WHERE o.user.userid =?1")
	List<Order> findByIdAccount(Integer id);

	@Query("SELECT o FROM Order o WHERE o.orderdate = ?1")
	List<Order> findByNgayTao(LocalDateTime ngayTao);

	@Query("SELECT o FROM Order o WHERE o.user.email =?1")
	List<Order> findByEfindByIdAccountmail(String email);

	@Query("SELECT new ReportRevenue(o.statusOrder.name, count(o)) FROM Order o GROUP BY o.statusOrder.name")
	List<ReportRevenue> countOrdersByStatus();

	// lịch sử đơn hàng

	@Query("SELECT o FROM Order o WHERE o.user.email = ?1 AND o.statusOrder.name = ?2")
	List<Order> findByUserEmailAndStatus(String email, String status);

	@Query("SELECT NEW ReportYear(YEAR(o.orderdate), sum(od.totalprice)) " + "FROM Order o "
			+ "INNER JOIN o.orderDetail od " + "GROUP BY YEAR(o.orderdate)" + "ORDER BY YEAR(o.orderdate)")
	List<ReportYear> getYearRevenue();

	@Query("SELECT NEW com.greenfarm.entity.FindReportYear(m.month, coalesce(SUM(od.totalprice), 0)) "
			+ "FROM (SELECT 1 AS month UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 "
			+ "      UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12) AS m "
			+ "LEFT JOIN Order o ON m.month = MONTH(o.orderdate) AND YEAR(o.orderdate) = :year "
			+ "LEFT JOIN OrderDetail od ON o.Orderid = od.order.Orderid " + "GROUP BY m.month " + "ORDER BY m.month")
	List<FindReportYear> findYearlyRevenue(@Param("year") Integer year);

	@Query("SELECT o FROM Order o WHERE o.user.email = ?1 AND o.statusOrder.name = ?2")
	List<Order> findByUserEmailAndStatusOrder_Name(String userEmail, String statusName);

	@Query("SELECT new CategorySalesByDate(pc.categoryname, COUNT(DISTINCT o.Orderid) as quantitySold) "
			+ "FROM Order o " + "JOIN o.orderDetail od " + "JOIN od.product p " + "JOIN p.category pc "
			+ "WHERE DATE(o.orderdate) = :date " + "GROUP BY pc.categoryname " + "UNION "
			+ "SELECT new CategorySalesByDate(pc.categoryname, 0 as quantitySold) " + "FROM Category pc "
			+ "WHERE pc NOT IN (SELECT DISTINCT p.category FROM Order o JOIN o.orderDetail od JOIN od.product p WHERE DATE(o.orderdate) = :date)")
	List<CategorySalesByDate> getCategorySalesByDate(
			@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date);

	@Query("SELECT NEW com.greenfarm.entity.Report7day(DATE(o.orderdate) AS date, SUM(od.totalprice) AS sum) "
			+ "FROM Order o " + "JOIN OrderDetail od ON o.Orderid = od.order.Orderid "
			+ "WHERE o.orderdate BETWEEN DATEADD(DAY, -7, CURRENT_DATE) AND CURRENT_DATE "
			+ "GROUP BY DATE(o.orderdate) " + "ORDER BY DATE(o.orderdate) DESC")
	List<Report7day> RevenueLast7Days();

}
