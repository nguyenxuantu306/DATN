package com.greenfarm.dto;

import java.util.List;

import com.greenfarm.entity.Booking;
import com.greenfarm.entity.Pricing;
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
	private String departureday;
	private String location;
	private Integer Availableslots;

	private User user;
	private List<Booking> booking;
	private TourConditionDTO tourCondition;
    private TourOverviewDTO tourOverview;
    private Pricing pricings;
    private List<TourImageDTO> tourImage;
    private List<CommentDTO> comment;
    private List<TourDateDTO> tourdate;
    
    private Boolean isdeleted = Boolean.FALSE;
     
}
