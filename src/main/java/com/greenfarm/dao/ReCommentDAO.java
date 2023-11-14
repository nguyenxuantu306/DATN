package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.Comment;
import com.greenfarm.entity.ReComment;

public interface ReCommentDAO extends JpaRepository<ReComment, Integer> {
	
	List<ReComment> findByComments(Comment comment);
	
}
