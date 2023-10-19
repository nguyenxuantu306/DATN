package com.greenfarm.entity;

import java.io.Serializable;

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

@SuppressWarnings("serial")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Products")
public class Product implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Productid;

	private String productname;
	
	private String Description;
		
	private Double price;
	
	private String image;
	
	private Integer quantityavailable;
	
	@ManyToOne
	@JoinColumn(name = "CategoryID")
	Category category;
	
//	@JsonIgnore
//	@OneToMany(mappedBy = "book")
//	List<OrderDetail> orderDetail;
	
//	@OneToMany
//	List<OrderDetail> orderDetail;
	
//	@OneToMany
//	List<ProductDiscount> productDiscount;
	
//	@OneToMany
//	List<ProductImage> productImage;
	
	
	@Override
	public String toString() {
		return "";
	}
}
