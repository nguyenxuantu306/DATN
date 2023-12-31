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
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Orders")
public class Order implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer Orderid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "orderdate")
	private LocalDateTime orderdate;

	public String getOrderDateFormatted() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm a");
		return orderdate.format(formatter);
	}

	@ManyToOne
	@JoinColumn(name = "AddressID")
	Address address;

	@JsonIgnore
	@OneToMany(mappedBy = "order")
	private List<OrderDetail> orderDetail;

	@ManyToOne
	@JoinColumn(name = "userid")
	User user;

	@ManyToOne
	@JoinColumn(name = "statusorderid")
	StatusOrder statusOrder;

	@ManyToOne
	@JoinColumn(name = "Paymentmethodid")
	PaymentMethod paymentmethod;

	@JsonIgnore
	@OneToMany(mappedBy = "order")
	List<VoucherOrder> voucherorder;
}
