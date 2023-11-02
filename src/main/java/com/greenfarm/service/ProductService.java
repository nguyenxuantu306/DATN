package com.greenfarm.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.greenfarm.entity.Product;
import com.greenfarm.entity.Report;

public interface ProductService {

	// Danh sách sản phẩm
	List<Product> findAll();

	// Phân trang
	Page<Product> findAll(Pageable pageable);

	// tìm sản phẩm theo id
	Product findById(Integer productid);

	// thêm sản phẩm
	Product create(Product Product);

	// cập nhật sản phẩm
	Product update(Product Product);

	// xóa sản phẩm
	void delete(Integer productid);

	// Tìm sản phẩm theo loại
	List<Product> findByCategoryId(String cid);

	// Tìm theo keyword
	List<Product> findProductByKeyword(String string);

	// Tìm theo khoảng giá
	List<Product> findProductByPriceRange(String priceRange);

	// Sắp Xếp theo tên A - Z , Z - A
	List<Product> findProductByProductNameSort(String sort);

	// Sắp xếp theo giá tăng & giảm
	List<Product> findProductByProductPiceSort(Integer sortprice);

	// Thống kê sản phẩm
	List<Report> getTk_sp();

	// Thống kê lại sp
	List<Report> getTk_loai();

}
