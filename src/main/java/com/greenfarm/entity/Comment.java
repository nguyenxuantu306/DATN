package com.greenfarm.ENTITY;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Comments")
public class Comment implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer CommentID;
	
//	@ManyToOne
//	@JoinColumn(name = "UserID")
//	Integer user;
//	
//	@ManyToOne	
//	@JoinColumn(name = "TourID")
//	Integer tour;
	
	String CommentText;
	
	Date CommentDate = new Date();
}
