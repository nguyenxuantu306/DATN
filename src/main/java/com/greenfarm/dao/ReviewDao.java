package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.greenfarm.entity.Product;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.Review;
import com.greenfarm.entity.StarCount;
import com.greenfarm.entity.User;

public interface ReviewDao extends JpaRepository<Review, Integer> {

		
	Page<Review> findByProduct(Product product, Pageable pageable);

	// Đếm sao
	@Query("SELECT DISTINCT new ReportRevenue(o.rating, count(o)) FROM Review o GROUP BY o.rating ORDER BY o.rating DESC")
	List<ReportRevenue> getRatingStats();

	@Query("SELECT r FROM Review r WHERE r.user = :user AND r.product = :product")
    Review findReviewByUserAndProduct(@Param("user") User user, @Param("product") Product product);

	@Query("SELECT NEW StarCount(ratings.rating, COALESCE(COUNT(reviews.rating), 0)) " +
		       "FROM (SELECT 1 AS rating UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) ratings " +
		       "LEFT JOIN Review reviews ON ratings.rating = reviews.rating AND reviews.product.productid = :productId " +
		       "GROUP BY ratings.rating " +
		       "ORDER BY ratings.rating DESC")
		List<StarCount> countReviewsByRating(@Param("productId") Integer productId);





}
