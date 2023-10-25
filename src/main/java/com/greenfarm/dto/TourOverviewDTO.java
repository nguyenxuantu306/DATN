package com.greenfarm.dto;

import com.greenfarm.entity.Tour;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourOverviewDTO {
	
	private Integer touroverviewid;
	private String Title;
	private String Content;
	private Tour tour;
}