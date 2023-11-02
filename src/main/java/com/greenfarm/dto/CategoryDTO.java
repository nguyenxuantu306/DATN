package com.greenfarm.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

	private Integer categoryid;
	private String categoryname;
	private String descriptions;
	private List<ProductDTO> products;
}