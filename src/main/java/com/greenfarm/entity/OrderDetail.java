package com.greenfarm.entity;

import java.io.Serializable;

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
public class OrderDetail implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer orderdetailid;

	@ManyToOne
	@JoinColumn(name = "Orderid")
	public Order order;

	@ManyToOne
	@JoinColumn(name = "productid")
	private Product product;

	Integer quantityordered;

	Float totalprice;


	
	public  Float getTotalPrice() {
	    return totalprice;
	}
	
	public void getTotalPrice(Float totalPrice) {
	    this.totalprice = totalPrice;
	}

	public void setTotalPrice(Float totalPrice) {
	    this.totalprice = totalPrice;
	}
}
