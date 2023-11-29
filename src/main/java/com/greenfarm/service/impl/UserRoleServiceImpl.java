package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.UserDAO;
import com.greenfarm.dao.UserRoleDAO;
import com.greenfarm.entity.Role;
import com.greenfarm.entity.User;
import com.greenfarm.entity.UserRole;
import com.greenfarm.service.RoleService;
import com.greenfarm.service.UserRoleService;

@Service
public class UserRoleServiceImpl implements UserRoleService {

	@Autowired
	UserRoleDAO dao;

	@Autowired
	UserDAO udao;
	
	@Autowired
	RoleService roleService;

	@Override
	public List<UserRole> findAll() {
		return dao.findAll();
	}

	@Override
	public UserRole create(UserRole auth) {
		return dao.save(auth);
	}

	@Override
	public void delete(Integer id) {
		dao.deleteById(id);

	}

	@Override
	public List<UserRole> findAuthoritesOfAdministrators() {
		List<User> users = udao.getAdministrators();
		return dao.authoritesOf(users);
	}

	@Override
	public UserRole createroleuser(User user) {
		// TODO Auto-generated method stub
		UserRole role = new UserRole();
		role.setUser(user);
		Role role2 = roleService.findByid(2);
		role.setRole(role2);
		return dao.save(role);
		
	}

}
