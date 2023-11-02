package com.greenfarm.dto;

import com.greenfarm.entity.Tour;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourConditionDTO {

	private Integer tourconditionid;
	private String conditions;
	private Tour tour;
}