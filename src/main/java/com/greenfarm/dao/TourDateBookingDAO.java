package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.TourDateBooking;

public interface TourDateBookingDAO extends JpaRepository<TourDateBooking, Integer> {

}
