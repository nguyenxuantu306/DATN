package com.greenfarm.dto;

import com.greenfarm.entity.Tour;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourImageDTO {
	
	private Integer tourimageid;
	private String Imageurl;
	private Tour tour;
}
