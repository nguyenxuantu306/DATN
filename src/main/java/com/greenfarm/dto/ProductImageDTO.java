package com.greenfarm.dto;

import com.greenfarm.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDTO {
	private Integer productimageid;
	private String imageurl;
	private Product product;
}
