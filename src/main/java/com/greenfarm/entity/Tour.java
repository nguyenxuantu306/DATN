package com.greenfarm.entity;

import java.io.Serializable;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
	@Positive(message = "TourID không được là số âm")
	private Integer tourid;
	
	@NotBlank(message = "Tour name không được phép trống (chỉ chứa khoảng trắng)")
	private String tourname;

	@NotBlank(message = "Description không được để trống")
	private String Description;

	@NotBlank(message = "Image URL không được để trống")
	private String image;
	
	@NotBlank(message = "Departure không được để trống")
	private String departureday;

	@NotBlank(message = "Location không được để trống")
	private String location;

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
	@OneToMany(mappedBy = "tour")
	private List<TourDate> tourdate;

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
	
	private Boolean isdeleted = Boolean.FALSE;
	public void setIsDeleted(boolean isdeleted) {
	    this.isdeleted = isdeleted;
	}
}
