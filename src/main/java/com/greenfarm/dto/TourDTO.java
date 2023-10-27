package com.greenfarm.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.greenfarm.entity.Comment;
import com.greenfarm.entity.Pricing;
import com.greenfarm.entity.TourCondition;
import com.greenfarm.entity.TourImage;
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
	private String location;
	private Integer Availableslots;
	private TourCondition tourCondition;
	private TourOverview tourOverview;
	private List<TourImage> tourImage;
	private Pricing pricings;
//	private UserDTO user;
//	private Set<Comment> comments = new HashSet<>();
}
