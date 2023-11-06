package com.greenfarm.service;

import java.util.List;

import com.greenfarm.entity.Product;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.Comment;
import com.greenfarm.entity.User;

public interface CommentService {

	// Create
	Comment create(Comment comment);
	
	// get all ratings
	List<Comment> getComments();
	
	List<Comment> commentbytourid(Tour tour);
	
	Comment findById(Integer commentid);
	
	Comment update(Comment comment);
	
	
	
	void deleteCommentById(Integer commentid);
	
	

	

}
