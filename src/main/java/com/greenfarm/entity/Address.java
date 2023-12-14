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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Address")
public class Address implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer AddressID;

	private String Street;
	private String Ward;
	private String District;
	private String City;
	private String Phonenumber;
	private String Fullname;
	private Boolean active = Boolean.FALSE;
	@ManyToOne
	@JoinColumn(name = "userid")
	private User user;
}
