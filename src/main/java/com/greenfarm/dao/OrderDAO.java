package com.greenfarm.dao;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.greenfarm.entity.FindReportYear;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.ReportYear;
import com.greenfarm.entity.RevenueTK;

public interface OrderDAO extends JpaRepository<Order, Integer> {

	@Query("SELECT o FROM Order o")
	List<Order> findAll(int offset, int limit);

	@Query("SELECT o FROM Order o WHERE o.user.userid =?1")
	List<Order> findByIdAccount(Integer id);

	@Query("SELECT o FROM Order o WHERE o.statusOrder.name = ?1")
	List<Order> findByStatusOrder_Name(String statusName);

	@Query("SELECT o FROM Order o WHERE o.orderdate = ?1")
	List<Order> findByNgayTao(LocalDateTime ngayTao);
	
	@Query("SELECT o FROM Order o WHERE o.user.email =?1")
	List<Order> findByEfindByIdAccountmail(String email);

	@Query("SELECT new ReportRevenue(o.statusOrder.name, count(o)) FROM Order o GROUP BY o.statusOrder.name")
	List<ReportRevenue> countOrdersByStatus();

	
	//lịch sử đơn hàng
    
    @Query("SELECT o FROM Order o WHERE o.user.email = ?1 AND o.statusOrder.name = ?2")
    List<Order> findByUserEmailAndStatus(String email, String status);

    
    
    @Query("SELECT NEW ReportYear(YEAR(o.orderdate), sum(od.totalprice)) " +
            "FROM Order o " +
            "INNER JOIN o.orderDetail od " +
            "GROUP BY YEAR(o.orderdate)" +
            "ORDER BY YEAR(o.orderdate)")
	List<ReportYear> getYearRevenue();

 
//
//    @Query("SELECT NEW FindReportYear(YEAR(o.orderdate), MONTH(o.orderdate), SUM(od.totalprice)) " +
//            "FROM Order o " +
//            "INNER JOIN o.orderDetail od " +
//            "WHERE YEAR(o.orderdate) = :year " +
//            "GROUP BY YEAR(o.orderdate), MONTH(o.orderdate) " +
//            "ORDER BY YEAR(o.orderdate), MONTH(o.orderdate)")
//    List<FindReportYear> findYearlyRevenue(@Param("year") Integer year);

    @Query(nativeQuery = true, value = 
    	    "SELECT  " +
    	    "    m.month AS monthValue, " +
    	    "    IFNULL(SUM(od.totalprice), 0) AS sum " +
    	    "FROM " +
    	    "    (SELECT 1 AS monthValue " +
    	    "     UNION SELECT 2 " +
    	    "     UNION SELECT 3 " +
    	    "     UNION SELECT 4 " +
    	    "     UNION SELECT 5 " +
    	    "     UNION SELECT 6 " +
    	    "     UNION SELECT 7 " +
    	    "     UNION SELECT 8 " +
    	    "     UNION SELECT 9 " +
    	    "     UNION SELECT 10 " +
    	    "     UNION SELECT 11 " +
    	    "     UNION SELECT 12) AS m " +
    	    "LEFT JOIN " +
    	    "    Order o ON m.month = MONTH(o.orderdate) AND YEAR(o.orderdate) = :year " +
    	    "LEFT JOIN " +
    	    "    Orderdetail od ON o.Orderid = od.orderid " +
    	    "GROUP BY " +
    	    "    m.month " +
    	    "ORDER BY " +
    	    "    m.month")
    	List<FindReportYear> findYearlyRevenue(@Param("year") Integer year);   
}
