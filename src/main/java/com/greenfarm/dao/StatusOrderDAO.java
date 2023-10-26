package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.greenfarm.entity.StatusOrder;

public interface StatusOrderDAO extends JpaRepository<StatusOrder, Integer> {

}
