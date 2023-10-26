package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.RoleDAO;
import com.greenfarm.entity.Role;
import com.greenfarm.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleDAO dao;

	@Override
	public List<Role> findAll() {

		return null;
	}

}
