package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.UserRole;



public interface UserRoleDAO extends JpaRepository<UserRole, Integer>{

}
