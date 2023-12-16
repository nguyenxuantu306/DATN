package com.greenfarm.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.greenfarm.entity.Product;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.Review;
import com.greenfarm.entity.StarCount;
import com.greenfarm.entity.User;

public interface ReviewService {

	// Create
	Review create(Review review);
	
	// get all ratings
	List<Review> getReviews();
	
	Page<Review> findbyproduct(Product product, Pageable pageable);
	
	boolean deleteReviewById(Integer reviewid);

	List<ReportRevenue> getRatingStats();
	
	boolean hasUserReviewedProduct(User user, Product product);

	List<StarCount> countReviewsByRating(Integer productId);

	Page<Review> findByProductOrderByDateCreatedDesc(Product item, Pageable pageable);

}
