package com.greenfarm.service;

import java.util.List;

import com.greenfarm.entity.Comment;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.ReComment;
import com.greenfarm.entity.User;

public interface ReCommentService {

	List<ReComment> findAll();
		
	// Create
	ReComment create(ReComment ReComment);
	
	// get all ratings
	List<ReComment> getReComments();
	
	List<ReComment> ReCommentbyComments(Comment comment);
	
	ReComment findById(Integer ReCommentid);
	
	ReComment update(ReComment ReComment);
	
	
	
	void deleteReCommentById(Integer ReCommentid);
	
	

	

}
