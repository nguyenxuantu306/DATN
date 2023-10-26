package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.UserRole;

public interface RoleDAO extends JpaRepository<UserRole, Integer> {

}
