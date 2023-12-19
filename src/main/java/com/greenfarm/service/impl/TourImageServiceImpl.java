package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.TourImageDAO;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.TourImage;
import com.greenfarm.service.TourImageService;

@Service
public class TourImageServiceImpl implements TourImageService {
	@Autowired
	TourImageDAO dao;

	@Override
	public List<TourImage> findAll() {
		return dao.findAll();
	}

	@Override
	public void createAll(List<TourImage> tourImages) {
		dao.saveAll(tourImages);
	}

	@Override
	public void create(List<TourImage> tourImages) {
		dao.saveAll(tourImages);

	}

	@Override
	public TourImage save(TourImage tourImage) {
		return dao.save(tourImage);

	}

	@Override
	public void deleteByTourimageid(Integer tourimageid) {
		dao.deleteByTourimageid(tourimageid);

	}

	@Override
	public List<TourImage> findByTour(Tour tour) {
		return dao.findByTour(tour);
	}

	@Override
	public void update(TourImage tourImage) {
		dao.save(tourImage);
	}

	@Override
	public void delete(TourImage tourImage) {
		dao.delete(tourImage);
	}

	@Override
	public void deleteById(Integer tourimageid) {
		dao.deleteById(tourimageid);
	}

}
