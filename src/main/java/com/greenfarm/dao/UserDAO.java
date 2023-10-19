package com.greenfarm.DAO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.ENTITY.User;





public interface UserDAO extends JpaRepository<User, Integer>{
	
	Optional<User>  findByEmail(String email);
}
