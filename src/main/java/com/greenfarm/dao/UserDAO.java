package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.User;

public interface UserDAO extends JpaRepository<User, Integer>{

}
