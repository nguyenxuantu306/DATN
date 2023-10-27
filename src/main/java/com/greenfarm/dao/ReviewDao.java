package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.Review;


public interface ReviewDao extends JpaRepository<Review, Integer> {

//	List<Review> finByUserId(Integer userid);
//	
//	
//	List<Review> finByProductId(Integer productid);
}
