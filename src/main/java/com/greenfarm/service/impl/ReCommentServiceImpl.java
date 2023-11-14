package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.ReCommentDAO;
import com.greenfarm.entity.Comment;
import com.greenfarm.entity.ReComment;
import com.greenfarm.service.ReCommentService;

@Service
public class ReCommentServiceImpl implements ReCommentService{

	@Autowired
	ReCommentDAO reCommentDAO;
	
	@Override
	public List<ReComment> findAll() {
		// TODO Auto-generated method stub
		return reCommentDAO.findAll();
	}

	@Override
	public ReComment create(ReComment ReComment) {
		// TODO Auto-generated method stub
		return reCommentDAO.save(ReComment);
	}

	@Override
	public List<ReComment> getReComments() {
		// TODO Auto-generated method stub
		return reCommentDAO.findAll();
	}

	@Override
	public List<ReComment> ReCommentbyComments(Comment comment) {
		// TODO Auto-generated method stub
		return reCommentDAO.findByComments(comment);
	}

	@Override
	public ReComment findById(Integer ReCommentid) {
		// TODO Auto-generated method stub
		return reCommentDAO.findById(ReCommentid).get();
	}

	@Override
	public ReComment update(ReComment ReComment) {
		// TODO Auto-generated method stub
		return reCommentDAO.save(ReComment);
	}

	@Override
	public void deleteReCommentById(Integer ReCommentid) {
		// TODO Auto-generated method stub
		reCommentDAO.deleteById(ReCommentid);
	}



}
