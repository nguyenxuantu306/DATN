package com.greenfarm.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Bookings")
public class Booking implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Bookingid;
	
	@Temporal(TemporalType.DATE)
	private Date Bookingdate = new Date();
	
	private Integer Numparticipants;
	
	private Float Totalprice;
	
	@ManyToOne
	@JoinColumn(name = "userid")
	User user;
	
	@ManyToOne
	@JoinColumn(name = "tourid")
	Tour tour;
	
//	@ManyToOne
//	@JoinColumn(name = "statusbookingid")
//	StatusBooking statusBooking;
	
//	@ManyToOne
//	@JoinColumn(name = "tourtypeticketid")
//	TourTypeTicket tourTypeTicket;
	

}
