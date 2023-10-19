package com.greenfarm.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.User;





public interface UserDAO extends JpaRepository<User, Integer>{
	
	Optional<User>  findByEmail(String email);
}
