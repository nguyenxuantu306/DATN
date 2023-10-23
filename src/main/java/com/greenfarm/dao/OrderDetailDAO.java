package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenfarm.dto.TopSellingProductDTO;
import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.Top10;




public interface OrderDetailDAO extends JpaRepository<OrderDetail, Integer>{

	@Query("SELECT new Report(o, sum(o.totalprice * o.quantityordered), sum(o.quantityordered)) FROM OrderDetail o" 
			+ " GROUP BY o"
			+ " ORDER BY sum(o.totalprice *o.quantityordered) DESC")
			List<Report> reportTheoLuotMuaHang();

	
	@Query("SELECT new Top10(o.product, sum(o.quantityordered)) FROM OrderDetail "
			+ "o GROUP BY o.product ORDER BY sum(o.quantityordered) DESC")
	Page<Top10> getTop10(Pageable pegeable);
	
	
	
}
