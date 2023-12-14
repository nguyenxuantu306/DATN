package com.greenfarm.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "paymentmethods")
public class PaymentMethod implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer paymentmethodid;
	private String Methodname;
	private String Description;

	@JsonIgnore
	@OneToMany(mappedBy = "paymentmethod")
	List<Order> order;

	@JsonIgnore
	@OneToMany(mappedBy = "paymentmethod")
	List<Booking> booking;
}