package com.greenfarm.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Bookings")
public class Booking implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Bookingid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "bookingdate")
	private LocalDateTime bookingdate;

	public String getBookingdateFormatted() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm a");
		return bookingdate.format(formatter);
	}

	private Float Totalprice;

	@NotNull(message = "Số lượng vé người lớn không được để trống")
	@Min(value = 1, message = "Số lượng vé người lớn phải lớn hơn hoặc bằng 1")
	private Integer Adultticketnumber;

	@NotNull(message = "Số lượng vé trẻ em không được để trống")
	@Min(value = 0, message = "Số lượng vé trẻ em không được nhỏ hơn 0")
	private Integer Childticketnumber;

	// @JsonIgnore
	// @OneToMany(mappedBy = "booking")
	// private List<TourDate> tourDate;

	private String qrcode;
	@ManyToOne
	@JoinColumn(name = "userid")
	User user;

	@ManyToOne
	@JoinColumn(name = "tourid")
	Tour tour;

	@ManyToOne
	@JoinColumn(name = "statusbookingid")
	StatusBooking statusbooking;

	@ManyToOne
	@JoinColumn(name = "paymentmethodid")
	PaymentMethod paymentmethod;

	@JsonIgnore
	@OneToOne(mappedBy = "booking")
	TourDateBooking tourDateBooking;

}
