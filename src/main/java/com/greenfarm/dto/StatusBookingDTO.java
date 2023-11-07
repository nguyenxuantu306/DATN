package com.greenfarm.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfarm.entity.Booking;

import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
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

	List<BookingDTO> booking;
}
