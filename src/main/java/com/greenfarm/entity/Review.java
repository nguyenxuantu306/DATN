package com.greenfarm.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Reviews")
public class Review implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reviewid;
	
	private String content;
	
	@Temporal(TemporalType.TIMESTAMP)
	LocalDateTime datepost;
	
	public String getDatePost() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm a");
		return datepost.format(formatter);
	}
	private Integer rating;
	
	@ManyToOne
	@JoinColumn(name = "userid")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "productid")
	private Product product;
	
}
