package com.greenfarm.dto;

import java.util.Date;

import com.greenfarm.entity.Category;
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
}
