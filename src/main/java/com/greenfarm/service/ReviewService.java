package com.greenfarm.service;

import java.util.List;

import com.greenfarm.entity.Review;

public interface ReviewService {

	// Create
	Review create(Review review);
	
	// get all ratings
	List<Review> getReviews();
	
//	// get all byuserid
//	List<Review> getReviewByUserId(Integer userid);
//	
//	// get all by productid
//	List<Review> getReviewByProductId(Integer productid);
}
