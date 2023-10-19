package com.greenfarm.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenfarm.entity.User;

public interface UserDAO extends JpaRepository<User, Integer>{

	@Query("SELECT DISTINCT ur.user FROM UserRole ur WHERE ur.role.id = 1")
	List<User> getAdministrators();
	
	// Security
	Optional<User>  findByEmail(String email);
}
