package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.greenfarm.entity.Category;
import com.greenfarm.entity.Product;

public interface CategoryDAO extends JpaRepository<Category, Integer> {
	@Query("SELECT p FROM Category p WHERE p.isdeleted = true")
	List<Category> findAllDeletedCategory();

	@Query("SELECT p FROM Category p WHERE p.categoryid = :id")
	Category findCategoryById(@Param("id") Integer id);

	// tìm kiếm keywword
	@Query("SELECT c FROM Category c WHERE LOWER(c.categoryname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.descriptions) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<Category> findByKeyword(@Param("keyword") String keyword);
	
	default void deleteByIsDeleted(Integer id) {
		Category category = findCategoryById(id);
		if (category != null) {
			category.setIsDeleted(true);
			save(category);
		}
	}

	List<Category> findAllByIsdeletedFalse();

	List<Category> findAllByIsdeletedTrue();

	@Modifying
	@Query("UPDATE Category b SET b.isdeleted = true WHERE b.categoryid = :id")
	void deleteCategoryById(@Param("id") Integer id);
}
