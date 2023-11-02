package com.greenfarm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfarm.entity.Category;

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
    
    private Category category;
    
}