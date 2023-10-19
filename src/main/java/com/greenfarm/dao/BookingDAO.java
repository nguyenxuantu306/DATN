package com.greenfarm.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.ENTITY.Booking;


public interface BookingDAO extends JpaRepository<Booking, Integer>{

}
