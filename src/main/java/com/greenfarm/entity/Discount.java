package com.greenfarm.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Discounts")
public class Discount implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Discountid;

	private String Name;

	private Float Value;

	@Temporal(TemporalType.DATE)
	private Date Startdate = new Date();

	@Temporal(TemporalType.DATE)
	private Date Enddate = new Date();

	@Temporal(TemporalType.DATE)
	private Date Createdate = new Date();

//	private Integer Amount;

	@JsonIgnore
	@OneToMany(mappedBy = "discount")
	List<ProductDiscount> productDiscounts;

	@ManyToOne
	@JoinColumn(name = "statusvoucherid")
	StatusVoucher statusVoucher;
}
