package com.greenfarm.service.impl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.TourDateDAO;
import com.greenfarm.dto.TourDTO;
import com.greenfarm.entity.Category;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.TourDate;
import com.greenfarm.service.TourDateService;

@Service
public class TourDateServiceImpl implements TourDateService {

	@Autowired
	TourDateDAO dao;
	
	@Override
	public List<TourDate> findAll() {
		return dao.findAll();
	}

    @Override
    public List<TourDate> findByDate(Date date) {
        return dao.findByTourdates(date);
    }
	
	@Override
	public TourDate create(TourDate tourdate) {
		return dao.save(tourdate);
	}
	
	

	@Override
	public TourDate update(TourDate tourdate) {
		return dao.save(tourdate);
	}

	@Override
	public void deleteTourDateById(Integer tourdateid) {
			dao.deleteById(tourdateid);
		
	}

	@Override
	public TourDate findById(Integer tourdateid) {
		return dao.findById(tourdateid).get();
	}



	@Override
	public TourDate findByTourAndTourdates(Tour tour, Date tourdates) {
		return dao.findByTourAndTourdates(tour, tourdates).get(0);
	}

	@Override
	public List<TourDate> findByKeyword(Integer keyword) {
		// TODO Auto-generated method stub
		return dao.findByKeyword(keyword);
	}
}
