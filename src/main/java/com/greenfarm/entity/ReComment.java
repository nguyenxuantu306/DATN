package com.greenfarm.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Recomments")
public class ReComment implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer recommentid;
	
	
	private String recommenttext;
	
	private Date recommentdate = new Date();
	
	
	@ManyToOne
	@JoinColumn(name = "userid")
	private User user;
	
	@ManyToOne	
	@JoinColumn(name = "commentid")
	Comment comment;
}
