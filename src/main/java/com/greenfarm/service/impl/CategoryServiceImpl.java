package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.greenfarm.dao.CategoryDAO;
import com.greenfarm.entity.Category;
import com.greenfarm.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryDAO dao;

	@Override
	public List<Category> findAll() {
		return dao.findAllByIsdeletedFalse();
	}

	@Override
	public Category findById(Integer categoryid) {
		return dao.findById(categoryid).get();
	}

	@Override
	public Category create(Category category) {
		return dao.save(category);
	}

	@Override
	public Category update(Category category) {
		return dao.save(category);
	}

	@Override
	public Category save(Category category) {
		return dao.save(category);
	}

	
	@Override
	@Transactional
	public void deleteCategoryById(Integer categoryid) {
		dao.deleteCategoryById(categoryid);
		
	}

	@Override
	public List<Category> findAllDeletedCategory() {
		return dao.findAllByIsdeletedTrue();
	}

	@Override
	public List<Category> findByKeyword(String keyword) {
		return dao.findByKeyword(keyword);
	}


}
