package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.Securetoken;

public interface Securetokendao extends JpaRepository<Securetoken, Long> {
	Securetoken findByToken(String token);
	
	

	Long removeByToken(String token);
}
