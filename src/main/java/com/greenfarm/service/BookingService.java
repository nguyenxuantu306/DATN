package com.greenfarm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.greenfarm.entity.Booking;
import com.greenfarm.entity.Top10;
import com.greenfarm.entity.Top3;

public interface BookingService {

	Page<Top3> getTop3Tour(Pageable pageable1);



}
