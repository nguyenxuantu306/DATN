package com.greenfarm.entity;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Products")
public class Product implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer productid;

	private String productname;
	
	private String Description;
		
	private Float price;
	
	private String image;
	
	private Integer quantityavailable;
	
	@ManyToOne
	@JoinColumn(name = "CategoryID")
	Category category;
	
	@JsonIgnore
	@OneToMany(mappedBy = "product")
	List<OrderDetail> orderDetails;	

//	@JsonIgnore
//	@OneToMany(mappedBy = "product")
//	List<ProductDiscount> productDiscount;
	
//	@JsonIgnore
//	@OneToMany(mappedBy = "product")
//	List<ProductImage> productImage;
	
	@Override
	public String toString() {
		return "";
	}
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER,mappedBy = "product")
	List<Review> Review;
}
