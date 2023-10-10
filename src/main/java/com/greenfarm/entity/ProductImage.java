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
@Table(name = "ProductImages")
public class ProductImage implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer ImageID;

	@ManyToOne
	@JoinColumn(name = "ProductID")
	Product product;
	
	String ImageURL;
	
}
