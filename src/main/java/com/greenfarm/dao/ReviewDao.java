package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.greenfarm.entity.Product;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.Review;
import com.greenfarm.entity.User;


public interface ReviewDao extends JpaRepository<Review, Integer> {

		
	List<Review> findByProduct(Product product);

	// Đếm sao
	@Query("SELECT DISTINCT new ReportRevenue(o.rating, count(o)) FROM Review o GROUP BY o.rating ORDER BY o.rating DESC")
	List<ReportRevenue> getRatingStats();

	
	
	@Query("SELECT r FROM Review r WHERE r.user = :user AND r.product = :product")
    Review findReviewByUserAndProduct(@Param("user") User user, @Param("product") Product product);
}
