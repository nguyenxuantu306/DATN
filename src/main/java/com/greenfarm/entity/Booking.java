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
	Integer BookingID;
	
//	@ManyToOne
//	@JoinColumn(name = "UserID")
//	User user;
//	
//	@ManyToOne
//	@JoinColumn(name = "TourID")
//	Integer tour;
	
	@Temporal(TemporalType.DATE)
	Date BookingDate = new Date();
	
	Integer NumParticipants;
	
	Float TotalPrice;
	
	String Status;
}
