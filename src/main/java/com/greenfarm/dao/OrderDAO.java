package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.greenfarm.entity.Order;

public interface OrderDAO extends JpaRepository<Order, Integer>{
	
	@Query("SELECT o FROM Order o")
    List<Order> findAll(int offset, int limit);

	@Query("SELECT o FROM Order o WHERE o.user.userid =?1")
	List<Order> findByIdAccount(Integer id);
	
	@Query("SELECT o FROM Order o WHERE o.statusOrder.name = ?1")
	List<Order> findByStatusOrder_Name(String statusName);
	
	@Query("SELECT o FROM Order o WHERE o.orderdate = ?1")
	List<Order> findByNgayTao(String ngayTao);
}
