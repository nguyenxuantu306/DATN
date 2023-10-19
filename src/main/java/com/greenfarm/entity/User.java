package com.greenfarm.entity;

import java.io.Serializable;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "Users")
public class User implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer userid;
	String password;
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
	@OneToMany(mappedBy = "user")
	List<UserRole> userRole;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Tour> tour;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Order> order;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Userdiscount>  discount;
}
