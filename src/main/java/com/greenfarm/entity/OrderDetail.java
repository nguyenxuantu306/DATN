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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Orderdetails")
public class OrderDetail implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer orderdetailid;
	
	@ManyToOne
	@JoinColumn(name = "orderid")
	Order order;
	
	@ManyToOne
	@JoinColumn(name = "productid")
	Product product;
	
	Integer quantityordered;
	
	Float totalprice;

	@ManyToOne
	@JoinColumn(name = "paymentmethodid")
	PaymentMethod paymentMethod;
	
	
	
}
