package com.greenfarm.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfarm.dao.OrderDAO;
import com.greenfarm.dao.OrderDetailDAO;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.Report;
import com.greenfarm.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	OrderDAO dao;

	@Autowired
	OrderDetailDAO ddao;
	
	@Override
	public List<Order> getAllOrders(int page, int size) {
		int offset = page * size;
		return dao.findAll(offset, size);
	}
	
//	@Override
//	public List<Order> findAll() {
//		return dao.findAll();
//	}

	@Override
	public Order create(JsonNode orderData) {
		ObjectMapper mapper = new ObjectMapper();

		Order order = mapper.convertValue(orderData, Order.class);
		dao.save(order);

		TypeReference<List<OrderDetail>> type = new TypeReference<List<OrderDetail>>() {};
		List<OrderDetail> details = mapper.convertValue(orderData.get("orderDetail"), type).stream()
				.peek(d -> d.setOrder(order)).collect(Collectors.toList());
		ddao.saveAll(details);

		return order;
	}

	

	

	@Override
	public List<Order> findByIdAccount(Integer Id) {
		// TODO Auto-generated method stub
		return dao.findByIdAccount(Id);
	}


	@Override
	public Order findById(Integer Id) {
		// TODO Auto-generated method stub
		return dao.findById(Id).get();
	}

	@Override
	public Order update(Order order) {
		// TODO Auto-generated method stub
		return dao.save(order);
	}
	
	
	@Override
    public List<Order> getOrdersByStatusName(String statusName) {
        return dao.findByStatusOrder_Name(statusName);
    }

	
	@Override
    public List<Order> filterOrdersByNgayTao(Date ngayTao) {
        // Thực hiện truy vấn để lọc các đơn hàng theo ngày tạo
        return dao.findByNgayTao(ngayTao);
    }

	@Override
	public List<Report> slstatus() {	
		return dao.countOrdersByStatus();
	}
}
