package com.greenfarm.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenfarm.entity.Review;
import com.greenfarm.service.ReviewService;

@RestController
@RequestMapping("/reviews")
public class RatingController {
	
	@Autowired
	private ReviewService reviewService;
	
    @PostMapping
	public ResponseEntity<Review> create(@RequestBody Review reiview){
		return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.create(reiview));
	}
	@GetMapping
	public ResponseEntity<List<Review>> getReviews(){
		return ResponseEntity.ok(reviewService.getReviews());
	}
//	@GetMapping("/users/{userid}")
//	public ResponseEntity<List<Review>> getReviewsByUserId(Integer userid){
//		return ResponseEntity.ok(reviewService.getReviewByUserId(userid));
//	}
//	
//	
//	@GetMapping("/products/{productid}")
//	public ResponseEntity<List<Review>> getReviewsByProductId(Integer productid){
//		return ResponseEntity.ok(reviewService.getReviewByProductId(productid));
//	}
}
