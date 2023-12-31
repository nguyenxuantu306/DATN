package com.greenfarm.dto;


import com.greenfarm.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
	private Integer AddressID;
	
	private String Street;
	private String Ward;
	private String District;
	private String City;
	private String Phonenumber;
	private String Fullname;
	private Boolean active = Boolean.FALSE;
	private User user;
	
	
}
