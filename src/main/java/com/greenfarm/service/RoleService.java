package com.greenfarm.service;

import java.util.List;

import com.greenfarm.entity.Role;

public interface RoleService {

	public List<Role> findAll();
	
	public Role findByid(Integer id);
}
