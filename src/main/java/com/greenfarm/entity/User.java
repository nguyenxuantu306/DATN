package com.greenfarm.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
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
@Table(name = "Users")
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
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
	
	@OneToMany
	List<Booking> booking;
	
	@OneToMany
	List<Comment> comment;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	List<UserRole> userRole;
}
