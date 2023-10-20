package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenfarm.entity.User;
import com.greenfarm.entity.UserRole;



public interface UserRoleDAO extends JpaRepository<UserRole, Integer>{

	@Query("SELECT DISTINCT u FROM UserRole u WHERE u.user IN ?1")
	List<UserRole> authoritesOf(List<User> users);
}
