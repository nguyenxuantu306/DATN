package com.greenfarm.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	private Integer productid;
	private String productname;
	private String description;
	private Double price;
	private String image;
	private Integer quantityavailable;
	@JsonIgnore
	private CategoryDTO category;

	// top 10
	private BigDecimal revenue;
}