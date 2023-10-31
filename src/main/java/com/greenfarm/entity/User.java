package com.greenfarm.entity;

import java.io.Serializable;


import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfarm.dto.Provider;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Table(name = "users")
public class User implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer userid;
	String password;
	@Column(unique = true)
	String email;
	String firstname;
	String lastname;
	String phonenumber;
	String image;
	String address;
	Boolean gender;
	@Temporal(TemporalType.DATE)
	Date birthday = new Date();
	
	@Temporal(TemporalType.DATE)
	Date createddate = new Date();
	
	//Boolean IsActive;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Booking> booking;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Comment> comment;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	List<UserRole> userRole;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Tour> tour;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Order> order;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<UserDiscount>  discount;
	
	private boolean accountVerified;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy = "user")
    private Set<Securetoken> tokens;
	
	@Enumerated(EnumType.STRING)
    private Provider provider;
}

