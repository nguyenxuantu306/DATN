package com.greenfarm.dto;

import java.util.Date;
import java.util.List;

import com.greenfarm.entity.Pricing;
import com.greenfarm.entity.TourImage;
import com.greenfarm.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TourDTO {
	private Integer tourid;
	private String Tourname;
	private String Description;
	private String image;
	private Date startdate = new Date();
	private Date enddate = new Date();
	private String location;
	private Integer Availableslots;
	
	private User user;
	
	private TourConditionDTO tourCondition;
    private TourOverviewDTO tourOverview;
    private Pricing pricings;
    private List<TourImageDTO> tourImage;
     
}
