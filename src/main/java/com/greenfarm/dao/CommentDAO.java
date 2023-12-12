package com.greenfarm.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.greenfarm.entity.Comment;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.User;

public interface CommentDAO extends JpaRepository<Comment, Integer> {

	List<Comment> findByUserAndCommentdate(User user, Date commentdate);

	List<Comment> findByTour(Tour tour);

	List<Comment> findAllByOrderByCommentdateDesc();

	// tìm kiếm keywword
	@Query("SELECT co FROM Comment co WHERE LOWER(co.commenttext) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<Comment> findByKeyword(@Param("keyword") String keyword);

}
