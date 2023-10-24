package com.greenfarm.service;

import java.util.List;
import com.greenfarm.entity.User;

public interface UserService {

	List<User> findAll();
	
	User findById(Integer userid);
	
	User create(User user);
	
	User update(User user);
	
	void delete(Integer userid);
	
	// Security
	User findByEmail(String email);
	
	public List<User> getAdministrators();
	
}
