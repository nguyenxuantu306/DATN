package com.greenfarm.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.ENTITY.Comment;


public interface CommentDAO extends JpaRepository<Comment, Integer>{

}
