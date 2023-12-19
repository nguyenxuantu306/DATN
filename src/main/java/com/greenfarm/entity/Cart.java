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

@SuppressWarnings("serial")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Cart")
public class Cart implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cartID;

	private Float quantity;

	@ManyToOne
	@JoinColumn(name = "Productid")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "userid")
	private User user;

}
