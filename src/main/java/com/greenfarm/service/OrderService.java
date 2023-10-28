package com.greenfarm.service;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.greenfarm.entity.Order;

public interface OrderService {

	Order create(JsonNode orderData);

//	List<Order> findAll();

	List<Order> findByIdAccount(Integer Id);

	Order findById(Integer Id);

	List<Order> getAllOrders(int page, int size);

	Order update(Order order);
	
	List<Order> getOrdersByStatusName(String statusName);
	
	List<Order> filterOrdersByNgayTao(Date ngayTao);


}
