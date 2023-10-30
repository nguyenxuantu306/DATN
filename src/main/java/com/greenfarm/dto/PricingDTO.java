package com.greenfarm.dto;

import com.greenfarm.entity.Tour;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PricingDTO {
	private Integer pricingid;
	private Float adultprice;
	private Float childprice;
	private Float infantprice;
	private Tour tour;
}
