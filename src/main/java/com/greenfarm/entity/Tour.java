package com.greenfarm.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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

	private String image;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "Startdate")
	private Date startdate = new Date();

	@Temporal(TemporalType.DATE)
	@Column(name = "Enddate")
	private Date enddate = new Date();
	
	
	private String location;
	
	@Column(name = "Availableslots")
	private Integer Availableslots;
	
	@ManyToOne
	@JoinColumn(name = "userid")
	User user;
	
	@JsonIgnore
	@OneToMany(mappedBy = "tour")
	List<Booking> booking;
	
	@JsonIgnore
	@OneToMany(mappedBy = "tour" ,cascade = CascadeType.ALL)
	private Set<Comment> comments = new HashSet<>();
//	List<Comment> comment;
	
	@JsonIgnore
	@OneToOne(mappedBy = "tour")
	TourCondition tourCondition;
	
	@JsonIgnore
	@OneToOne(mappedBy = "tour")
	TourOverview tourOverview;
	
	@JsonIgnore
	@OneToMany(mappedBy = "tour")
	List<TourImage> tourImage;
	
	@JsonIgnore
	@OneToOne(mappedBy = "tour")
	Pricing pricings;
	
	@Override
	public String toString() {
		return "";
	}
}
