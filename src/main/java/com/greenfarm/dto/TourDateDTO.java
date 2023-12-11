package com.greenfarm.dto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.greenfarm.entity.Tour;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourDateDTO {

	private Integer tourdateid;
	private Date tourdates;
	private Integer availableslots;
	private Tour tour;
}