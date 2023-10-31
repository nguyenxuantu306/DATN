package com.greenfarm.service;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.ReportRevenue;

public interface OrderService {

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
	List<Order> filterOrdersByNgayTao(Date ngayTao);

	// Thống kê số lượng trạng thái
	List<ReportRevenue> slstatus();
	
	void cancelOrder(Integer orderId);

	List<Order> findAll();
}
