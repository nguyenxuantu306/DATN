package com.greenfarm.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfarm.entity.Booking;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.PaymentMethod;
import com.greenfarm.entity.StatusBooking;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.User;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDTO {
	private Integer Paymentmethodid;
	private String Methodname;
	private String Description;
	private List<Order> order;
	private List<Booking> booking;
}
