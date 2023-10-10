package com.greenfarm.dto;

import com.greenfarm.entity.Category;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private Float price;
    private String image;
    private Integer quantityavailable;
    private CategoryDTO category;
}