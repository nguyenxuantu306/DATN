package com.greenfarm.mapper;

import org.mapstruct.Mapper;

import com.greenfarm.DTO.UserDTO;
import com.greenfarm.ENTITY.User;

@Mapper
public interface Usermapper {
	
	
	UserDTO toDto(User user);
	User fromDto(UserDTO userDto);
}
