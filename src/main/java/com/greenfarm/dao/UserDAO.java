package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenfarm.entity.User;

public interface UserDAO extends JpaRepository<User, Integer>{

//	@Query("SELECT DISTINCT ur.user FROM UserRoles ur WHERE ur.role.Id IN (1)")
//	List<User> getAdministrators();
	
	@Query("SELECT DISTINCT ur.user FROM UserRole ur WHERE ur.role.id = 1")
	List<User> getAdministrators();
	
//	@Query("SELECT DISTINCT u FROM UserRole ur JOIN ur.user u WHERE ur.role.id = 1")
//	List<User> getAdministrators();

}
