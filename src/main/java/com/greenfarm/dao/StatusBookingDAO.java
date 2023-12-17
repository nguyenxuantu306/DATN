package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.greenfarm.entity.StatusBooking;
import com.greenfarm.entity.StatusOrder;

public interface StatusBookingDAO extends JpaRepository<StatusBooking, Integer> {

	StatusBooking getStatusbookingByStatusbookingid(int canceledStatusOrderId);

}
