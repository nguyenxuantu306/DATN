package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.ReviewDao;
import com.greenfarm.entity.Review;
import com.greenfarm.service.ReviewService;


@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewDao dao;
	
	
	@Override
	public Review create(Review review) {
		return dao.save(review);
	}

	@Override
	public List<Review> getReviews() {
		return dao.findAll();
	}

//	@Override
//	public List<Review> getReviewByUserId(Integer userid) {
//		return dao.finByUserId(userid);
//	}
//
//	@Override
//	public List<Review> getReviewByProductId(Integer productid) {
//		return dao.finByProductId(productid);
//	}

}
