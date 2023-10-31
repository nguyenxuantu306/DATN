package com.greenfarm.restcontroller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenfarm.entity.Report;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.Review;
import com.greenfarm.exception.UnkownIdentifierException;
import com.greenfarm.service.ReviewService;
import com.greenfarm.service.UserService;

@RestController
@RequestMapping("/rest/rating")
public class RatingRestController {

	@Autowired
	private ReviewService reviewService;

	@Autowired
	UserService userService;

	@PostMapping
	public ResponseEntity<Review> create(@RequestBody Review reiview) throws UnkownIdentifierException {
		return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.create(reiview));
	}

	@GetMapping
	public ResponseEntity<List<Review>> getReviews() {
		return ResponseEntity.ok(reviewService.getReviews());
	}

	@GetMapping("/stats")
	public List<ReportRevenue> getRatingStats() {
		return reviewService.getRatingStats();
	}

}
