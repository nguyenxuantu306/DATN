package com.greenfarm.dao;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenfarm.entity.Order;
import com.greenfarm.entity.ReportRevenue;

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
}
