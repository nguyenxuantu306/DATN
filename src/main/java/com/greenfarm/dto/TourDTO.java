package com.greenfarm.dto;

import java.util.Date;
import com.greenfarm.entity.Pricing;
import com.greenfarm.entity.TourCondition;
import com.greenfarm.entity.TourOverview;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourDTO {
	
	private Integer tourid;
	private String Tourname;
	private String Description;
	private String image;
	private Date startdate = new Date();
	private Date enddate = new Date();
	private Double price;
	private String location;
	private Integer Availableslots;
	private TourCondition tourCondition;
	private TourOverview tourOverview;
	private Pricing pricings;
}
