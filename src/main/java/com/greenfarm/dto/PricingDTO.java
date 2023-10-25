package com.greenfarm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfarm.entity.Tour;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PricingDTO {

	private Integer pricingid;
	private Tour tour;
	private Integer Adultprice;
	private Integer Childprice;
	private Integer Infantprice;
}