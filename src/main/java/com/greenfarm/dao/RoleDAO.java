package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.greenfarm.entity.Role;

public interface RoleDAO extends JpaRepository<Role, Integer>{

}
