package com.greenfarm.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
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



@SuppressWarnings("serial")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Tours")
public class Tour implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer tourid;

	private String Tourname;

	private String Description;

	private String Image;
	
//	@Temporal(TemporalType.DATE)
//	@Column(name = "Startdate")
//	private Date Startdate = new Date();
//
//	@Temporal(TemporalType.DATE)
//	@Column(name = "Enddate")
//	private Date Enddate = new Date();
	
	private String Departureday;
	
	private Double Price;
	
	private String Location;
	
	@Column(name = "Availableslots")
	private Integer Availableslots;
	
	private String vehicle;
	
	@Column(name = "Icondition")
	private String icondition;
	
	
	private String programme;
	
	/* private boolean activee; */
	
	@Override
	public String toString() {
		return "";
	}
	@ManyToOne
	@JoinColumn(name = "UserID")
	User user;
//	@OneToMany
//	List<Booking> booking;
//	
//	@OneToMany
//	List<Comment> comment;
//	
//	@OneToMany
//	List<Pricing> pricing;
//	
//	@OneToOne
//	TourCondition tourCondition;
//	
//	@OneToOne
//	TourOverview tourOverview;
//	
//	@OneToMany
//	List<TourImage> tourImage;
	
//	@OneToMany(mapped = "tour")
//	List<TourTypeTicket> tourTypeTicket;
}
