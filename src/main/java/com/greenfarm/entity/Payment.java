package com.greenfarm.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "Payments")
public class Payment implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer PaymentID;
	
	Integer UserID;
	
	Integer OrderID;
	
	Integer PaymentMethodID;
	
	@Temporal(TemporalType.DATE)
	Date PaymentDate = new Date();
	
	Float Amount;
	
	String TransactionID;
	
	String Status;
}
