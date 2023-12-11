package com.greenfarm.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.Top10;

public interface OrderDetailService {
	// Lấy ra all HDCT
	List<OrderDetail> findAll();

	// Tổng doanh thu đơn hàng
	List<Report> tongdoanhthu();

	Page<Top10> getTop10(Pageable pageable);

}
