package com.greenfarm.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.ReportRevenue;

public interface OrderService {
	List<Order> findOrdersByDateRange(LocalDateTime atStartOfDay, LocalDateTime plusDays, int page, int size);
	
	Order create(JsonNode orderData);

	
//	List<Order> findAll();

	List<Order> findByIdAccount(Integer Id);

	Order findById(Integer Id);

	List<Order> findByEfindByIdAccountmail(String email);

	List<Order> getAllOrders(int page, int size);

	Order update(Order order);

	// Hàm Lọc lấy tên theo trạng thái
	List<Order> getOrdersByStatusName(String statusName);

	// Hàm lọc theo ngày tạo
	List<Order> filterOrdersByNgayTao(LocalDateTime ngayTao);

	// Thống kê số lượng trạng thái
	List<ReportRevenue> slstatus();

	void cancelOrder(Integer orderId);

	List<Order> findAll();

	// lọc trạng thái trong history_order
	List<Order> findByUserEmailAndStatus(String email, String status);


	
}
