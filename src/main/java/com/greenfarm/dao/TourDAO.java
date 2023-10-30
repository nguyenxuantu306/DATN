package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.Tour;

public interface TourDAO extends JpaRepository<Tour, Integer> {

}
