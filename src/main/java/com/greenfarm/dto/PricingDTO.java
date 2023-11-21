package com.greenfarm.dto;

import com.greenfarm.entity.Tour;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PricingDTO {
	private Integer pricingid;
	private Float adultprice;
	private Float childprice;
//	private Float infantprice;
	private Tour tour;
}
