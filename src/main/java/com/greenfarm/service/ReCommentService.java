package com.greenfarm.service;

import java.util.List;

import com.greenfarm.entity.Comment;
import com.greenfarm.entity.ReComment;

public interface ReCommentService {

	List<ReComment> findAll();

	// Create
	ReComment create(ReComment ReComment);

	// get all ratings
	List<ReComment> getReComments();

	List<ReComment> getrecommenComments(Comment comment);

	ReComment findById(Integer ReCommentid);

	ReComment update(ReComment ReComment);

	void deleteReCommentById(Integer ReCommentid);
}
