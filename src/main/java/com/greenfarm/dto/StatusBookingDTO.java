package com.greenfarm.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusBookingDTO {

	private Integer statusbookingid;
	private String name;
	private List<BookingDTO> booking;
}
