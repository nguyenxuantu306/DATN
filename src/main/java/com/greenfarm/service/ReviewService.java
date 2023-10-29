package com.greenfarm.service;

import java.util.List;

import com.greenfarm.entity.Product;
import com.greenfarm.entity.Review;

public interface ReviewService {

	// Create
	Review create(Review review);
	
	// get all ratings
	List<Review> getReviews();
	
	List<Review> findbyproduct(Product product);
	
	boolean deleteReviewById(Integer reviewid);
}
