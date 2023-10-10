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
	Integer UserID;
	String Username;
	String Password;
	String Email;
	String FirstName;
	String LastName;
	String PhoneNumber;
	String Image;
	String Address;
	Boolean Gender;
	@Temporal(TemporalType.DATE)
	Date Birthday = new Date();
	
	@Temporal(TemporalType.DATE)
	Date CreatedDate = new Date();
	
	Boolean IsActive;
	
	@OneToMany
	List<Booking> booking;
	
	@OneToMany
	List<Comment> comment;
	
	@OneToMany
	List<UserRole> userRole;
}
