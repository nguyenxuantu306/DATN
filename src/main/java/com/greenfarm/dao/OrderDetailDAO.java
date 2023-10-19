package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.Report;


public interface OrderDetailDAO extends JpaRepository<OrderDetail, Integer>{

	@Query("SELECT new Report(o, sum(o.totalprice * o.quantityordered), sum(o.quantityordered)) FROM OrderDetail o" 
			+ " GROUP BY o"
			+ " ORDER BY sum(o.totalprice *o.quantityordered) DESC")
			List<Report> reportTheoLuotMuaHang();
}
