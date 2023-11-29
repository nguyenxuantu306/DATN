package com.greenfarm.dto;


import com.greenfarm.entity.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDTO {
	private Integer AddressID;
	
	private String Street;
	private String District;
	private String City;
	
	private User user;
	private Boolean Active = Boolean.FALSE;
	
}
