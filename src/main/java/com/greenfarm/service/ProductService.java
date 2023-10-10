package com.greenfarm.service;

import java.util.List;




import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.greenfarm.entity.Product;




public interface ProductService {

	// Danh sách sản phẩm
	List<Product> findAll();

	// tìm sản phẩm theo id
	Product findById(Integer productid);

	// thêm sản phẩm
	Product create(Product Product);

	// cập nhật sản phẩm
	Product update(Product Product);

	// xóa sản phẩm
	void delete(Integer productid);

	

	

	List<Product> findProductByKeyword(String string);

	 List<Product> findProductByPriceRange(Double minPrice, Double maxPrice);

	List<Product> findProductByPriceRange(String priceRange);

	List<Product> findProductByProductNameSort(String sort);

	
}
