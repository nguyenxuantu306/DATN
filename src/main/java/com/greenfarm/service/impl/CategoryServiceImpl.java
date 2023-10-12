package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.CategoryDAO;
import com.greenfarm.entity.Category;
import com.greenfarm.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryDAO dao;

	@Override
	public List<Category> findAll() {		
		return dao.findAll();
	}
	
	
}