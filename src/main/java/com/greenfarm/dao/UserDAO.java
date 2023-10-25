package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenfarm.entity.User;


public interface UserDAO extends JpaRepository<User, Integer>{

	@Query("SELECT DISTINCT ur.user FROM UserRole ur WHERE ur.role.id IN (1,2)")
	List<User> getAdministrators();
	
//	// Security
//	Optional<User>  findByEmail(String email);
	//User findByEmail(String email);
	
	Optional<User>  findByEmail(String email);
}
