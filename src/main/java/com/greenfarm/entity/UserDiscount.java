package com.greenfarm.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
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
@Table(name = "Userdiscounts")
public class UserDiscount implements Serializable{
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userdiscountid;
	
	@ManyToOne
	@JoinColumn(name = "userid")
	User user;
	
	@ManyToOne
	@JoinColumn(name = "discountid")
	Discount discount;
	
}
