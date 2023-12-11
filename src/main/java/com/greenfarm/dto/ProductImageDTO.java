package com.greenfarm.dto;

import com.greenfarm.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductImageDTO {
	private Integer productimageid;
	private String imageurl;
	private Product product;
	
	public ProductImageDTO() {
	}
	
	public ProductImageDTO(String imageurl) {
		super();
		this.imageurl = imageurl;
	}
	
	
}
