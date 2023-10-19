package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.Comment;


public interface CommentDAO extends JpaRepository<Comment, Integer>{

}
