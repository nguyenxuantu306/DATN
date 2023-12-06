package com.greenfarm.dto;

import java.util.Date;
import java.util.List;

import com.greenfarm.entity.Booking;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.TourDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourDateBookingDTO {

	private Integer tourdatebookingid;
	private TourDate tourdate;
	private Booking booking;
}