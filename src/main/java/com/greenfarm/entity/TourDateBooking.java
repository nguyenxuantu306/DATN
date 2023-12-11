package com.greenfarm.entity;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Tourdatebooking")
public class TourDateBooking implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer tourdatebookingid;
	
	
	@ManyToOne
	@JoinColumn(name = "TourdateID")
	private TourDate tourdate;
	
	@ManyToOne
	@JoinColumn(name = "BookingID")
	private Booking booking;
}
