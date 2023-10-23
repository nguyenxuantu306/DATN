package com.greenfarm.dto;

import com.greenfarm.entity.Tour;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourImageDTO {
	
	private Integer tourimageid;
	private String imageurl;
	private Tour tour;
}
