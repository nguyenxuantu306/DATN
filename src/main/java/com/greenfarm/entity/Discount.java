package com.greenfarm.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Discount implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer DiscountID;
	
	String Name;
	
	Float Value;
	
	@Temporal(TemporalType.DATE)
	Date StartDate = new Date();
	
	@Temporal(TemporalType.DATE)
	Date EndDate = new Date();
	
	@Temporal(TemporalType.DATE)
	Date CreateDate = new Date();
	
	String Status;
	
	@OneToMany
	List<ProductDiscount> productDiscounts;
}
