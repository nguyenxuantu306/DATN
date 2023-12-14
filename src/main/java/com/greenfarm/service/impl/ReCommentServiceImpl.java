package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.ReCommentDAO;
import com.greenfarm.entity.Comment;
import com.greenfarm.entity.ReComment;
import com.greenfarm.service.ReCommentService;

@Service
public class ReCommentServiceImpl implements ReCommentService {

	@Autowired
	ReCommentDAO reCommentDAO;

	@Override
	public List<ReComment> findAll() {
		return reCommentDAO.findAll();
	}

	@Override
	public ReComment create(ReComment ReComment) {
		return reCommentDAO.save(ReComment);
	}

	@Override
	public List<ReComment> getReComments() {
		return reCommentDAO.findAll();
	}

	@Override
	public ReComment findById(Integer ReCommentid) {
		return reCommentDAO.findById(ReCommentid).get();
	}

	@Override
	public ReComment update(ReComment ReComment) {
		return reCommentDAO.save(ReComment);
	}

	@Override
	public void deleteReCommentById(Integer ReCommentid) {
		reCommentDAO.deleteById(ReCommentid);
	}

	@Override
	public List<ReComment> getrecommenComments(Comment comment) {
		return reCommentDAO.findByComment(comment);
	}

}
