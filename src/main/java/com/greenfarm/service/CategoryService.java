package com.greenfarm.service;

import java.util.List;

import com.greenfarm.entity.Category;

public interface CategoryService {
	// API show loại sản phẩm
	List<Category> findAll();

	// API tìm kiếm loại sản phẩm theo categoryid
	Category findById(Integer categoryid);

	// API thêm loại sản phẩm
	Category create(Category category);

	// API cập nhật sản phẩm
	Category update(Category category);

	Category save(Category category);

	void deleteCategoryById(Integer categoryid);

	List<Category> findAllDeletedCategory();
}
