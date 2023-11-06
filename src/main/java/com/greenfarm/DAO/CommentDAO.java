package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.Comment;
import com.greenfarm.entity.Tour;

public interface CommentDAO extends JpaRepository<Comment, Integer> {

	List<Comment> findByTour(Tour tour);
}
