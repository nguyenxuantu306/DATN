package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.greenfarm.entity.StatusBooking;

public interface StatusBookingDAO extends JpaRepository<StatusBooking, Integer> {

}
