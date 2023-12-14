package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.TourDateBookingDAO;
import com.greenfarm.entity.TourDateBooking;
import com.greenfarm.service.TourDateBookingService;

@Service
public class TourDateBookingServiceImpl implements TourDateBookingService {

	@Autowired
	TourDateBookingDAO dao;

	@Override
	public List<TourDateBooking> findAll() {
		return dao.findAll();
	}

	@Override
	public TourDateBooking create(TourDateBooking tourdatebooking) {
		return dao.save(tourdatebooking);
	}

	@Override
	public TourDateBooking update(TourDateBooking tourdatebooking) {
		return dao.save(tourdatebooking);
	}

	@Override
	public void deleteTourDateBookingById(Integer tourdatebookingid) {
		dao.deleteById(tourdatebookingid);

	}

	@Override
	public TourDateBooking findById(Integer tourdatebookingid) {
		return dao.findById(tourdatebookingid).get();
	}

}
