package com.greenfarm.dto;

import java.util.Date;

import com.greenfarm.entity.Comment;
import com.greenfarm.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReCommentDTO {
	private Integer recommentid;
	private String recommenttext;

	private Date recommentdate = new Date();

	private User user;

	private Comment comment;
}
