package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.CommentDAO;
import com.greenfarm.entity.Comment;
import com.greenfarm.entity.Tour;
import com.greenfarm.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService{

	@Autowired
	CommentDAO commentDAO;
	
	@Override
	public Comment create(Comment comment) {
		// TODO Auto-generated method stub
		return commentDAO.save(comment);
	}

	@Override
	public List<Comment> getComments() {
		// TODO Auto-generated method stub
		return commentDAO.findAll();
	}

	@Override
	public Comment findById(Integer commentid) {
		// TODO Auto-generated method stub
		return commentDAO.findById(commentid).get();
	}

	@Override
	public Comment update(Comment comment) {
		// TODO Auto-generated method stub
		return commentDAO.save(comment);
	}

	@Override
	public void deleteCommentById(Integer commentid) {
		commentDAO.deleteById(commentid);
	}

	@Override
	public List<Comment> commentbytourid(Tour tour) {
		// TODO Auto-generated method stub
		return commentDAO.findByTour(tour);
	}

	@Override
	public List<Comment> getCommentsOrderByDateDesc() {
	     return commentDAO.findAllByOrderByCommentdateDesc();
	} 

}
