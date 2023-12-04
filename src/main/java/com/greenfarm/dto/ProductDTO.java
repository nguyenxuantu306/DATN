package com.greenfarm.dto;

import java.util.List;

import com.greenfarm.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	private Integer productid;
    private String productname;
    private String description;
    private Double price;
    private String image;
    private Float quantityavailable;
	private Boolean isdeleted = Boolean.FALSE;
    private Category category;
    private List<ProductImageDTO> productimage;
}