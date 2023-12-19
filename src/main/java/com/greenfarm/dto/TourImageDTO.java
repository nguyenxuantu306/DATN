package com.greenfarm.dto;

import com.greenfarm.entity.Tour;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TourImageDTO {

	private Integer tourimageid;
	private String imageurl;
	private Tour tour;
	
	public TourImageDTO() {
	}
	
	public TourImageDTO(String imageurl) {
		super();
		this.imageurl = imageurl;
	}
	
}
