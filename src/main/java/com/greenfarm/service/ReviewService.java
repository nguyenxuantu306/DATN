package com.greenfarm.service;

import java.util.List;

import com.greenfarm.entity.Product;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.Review;
import com.greenfarm.entity.User;

public interface ReviewService {

	// Create
	Review create(Review review);
	
	// get all ratings
	List<Review> getReviews();
	
	List<Review> findbyproduct(Product product);
	
	boolean deleteReviewById(Integer reviewid);

	List<ReportRevenue> getRatingStats();
	
	boolean hasUserReviewedProduct(User user, Product product);

}
