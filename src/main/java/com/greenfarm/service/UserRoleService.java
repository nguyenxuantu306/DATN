package com.greenfarm.service;

import java.util.List;

import com.greenfarm.entity.User;
import com.greenfarm.entity.UserRole;

public interface UserRoleService {

	public List<UserRole> findAll();

	public UserRole create(UserRole auth);
	
	public UserRole createroleuser(User user);

	public void delete(Integer id);

	public List<UserRole> findAuthoritesOfAdministrators();

}
