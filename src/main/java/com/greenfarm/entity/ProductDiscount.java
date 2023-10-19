package com.greenfarm.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
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
@Table(name = "ProductDiscounts")
public class ProductDiscount implements Serializable{
	@Id
	
	@ManyToOne
	@JoinColumn(name = "ProductID")
	Product product;
	
	@ManyToOne
	@JoinColumn(name = "DiscountID")
	Discount discount;
}
