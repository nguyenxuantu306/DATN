package com.greenfarm.dao;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenfarm.entity.Booking;
import com.greenfarm.entity.Top10;
import com.greenfarm.entity.Top3;

public interface BookingDAO extends JpaRepository<Booking, Integer> {

	@Query("SELECT new Top3(o.tour, sum(o.Numparticipants)) FROM Booking "
			+ "o GROUP BY o.tour ORDER BY sum(o.Numparticipants) DESC")
	Page<Top3> getTop3Tour(Pageable pageable1);

}
