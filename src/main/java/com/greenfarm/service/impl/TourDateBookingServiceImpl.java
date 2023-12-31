package com.greenfarm.service.impl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.TourDateBookingDAO;
import com.greenfarm.dto.TourDTO;
import com.greenfarm.entity.Category;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.TourDate;
import com.greenfarm.entity.TourDateBooking;
import com.greenfarm.service.TourConditionService;
import com.greenfarm.service.TourDateBookingService;
import com.greenfarm.service.TourDateService;

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

	@Override
	public List<TourDateBooking> findByKeyword(String keyword) {
		return dao.findByKeyword(keyword);
	}

	@Override
	public List<TourDateBooking> findByDepartureDay(String departureday) {
		return dao.findByDepartureDay(departureday);
	}

	@Override
	public List<TourDateBooking> findByDate(Date date) {
		return dao.findByTourdates(date);
	}

	@Override
	public List<TourDateBooking> getBookingsForTourDate(TourDate tourDate) {
		return dao.findByTourdate(tourDate);
	}


}
