package com.greenfarm.dto;

import java.util.Date;
import java.util.List;

import com.greenfarm.entity.Category;
import com.greenfarm.entity.Comment;
import com.greenfarm.entity.ReComment;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
	private Integer commentid;
	private String Commenttext;
	
	private Date commentdate = new Date();
	
	private User user;
	
	private Tour tour;
	
	private List<ReCommentDTO> recomment;
}
