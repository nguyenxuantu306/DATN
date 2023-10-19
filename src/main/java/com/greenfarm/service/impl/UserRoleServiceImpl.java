package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.UserDAO;
import com.greenfarm.dao.UserRoleDAO;
import com.greenfarm.entity.User;
import com.greenfarm.entity.UserRole;
import com.greenfarm.service.UserRoleService;

@Service
public class UserRoleServiceImpl implements UserRoleService{

	@Autowired
	UserRoleDAO dao;
	
	@Autowired
	UserDAO udao;
	
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
		List<User> users  = udao.getAdministrators();
		return dao.authoritesOf(users);
	}

	

}
