package com.greenfarm.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	
	private String tourname;

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
	private User user;

	@JsonIgnore
	@OneToMany(mappedBy = "tour")
	private List<Booking> booking;

	@JsonIgnore
	@OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
	private List<Comment> comment;

	@JsonIgnore
	@OneToOne(mappedBy = "tour", orphanRemoval = true, cascade = CascadeType.ALL)
	private TourCondition tourCondition;

	@JsonIgnore
	@OneToOne(mappedBy = "tour", orphanRemoval = true, cascade = CascadeType.ALL)
	private TourOverview tourOverview;

	@OneToOne(mappedBy = "tour", orphanRemoval = true, cascade = CascadeType.ALL)
	private Pricing pricings;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tour")
	private List<TourImage> tourImage;

	@Override
	public String toString() {
		return "";
	}
}
