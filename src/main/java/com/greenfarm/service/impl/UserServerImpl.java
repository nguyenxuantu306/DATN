package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.UserDAO;
import com.greenfarm.entity.User;
import com.greenfarm.service.UserService;

@Service
public class UserServerImpl implements UserService{

	@Autowired
	UserDAO dao;
	
	@Override
	public List<User> findAll() {
		return dao.findAll();
	}

	@Override
	public User findById(Integer userid) {
		return dao.findById(userid).get();
	}

	@Override
	public User create(User user) {
		return dao.save(user);
	}

	@Override
	public User update(User user) {
		return dao.save(user);
	}

	@Override
	public void delete(Integer userid) {
		dao.deleteById(userid);
	}

}
