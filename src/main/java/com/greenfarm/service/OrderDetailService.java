package com.greenfarm.service;

import java.util.List;

import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.Report;


public interface OrderDetailService {
	// Lấy ra all HDCT
	List<OrderDetail> findAll();

	// Tổng doanh thu đơn hàng
	List<Report> tongdoanhthu();


}
