package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.Booking;

public interface BookingDAO extends JpaRepository<Booking, Integer>{

}
