package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenfarm.entity.Product;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.Review;


public interface ReviewDao extends JpaRepository<Review, Integer> {

		
	List<Review> findByProduct(Product product);

	// Đếm sao
	@Query("SELECT DISTINCT new ReportRevenue(o.rating, count(o)) FROM Review o GROUP BY o.rating")
	List<ReportRevenue> getRatingStats();
}
