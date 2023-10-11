package com.greenfarm.dao;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenfarm.entity.Product;


public interface ProductsDAO extends JpaRepository<Product, Integer>  {

	@Query("SELECT p FROM Product p WHERE p.productname like %?1%")
	List<Product> findProductByKeyword(String keyword);

	
	@Query("SELECT p FROM Product p WHERE p.price BETWEEN ?1 AND ?2")
	List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

	@Query("SELECT p FROM Product p WHERE p.category.categoryid=?1")
	List<Product> findByCategoryId(String cid);
}
