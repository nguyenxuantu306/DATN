package com.greenfarm.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.greenfarm.entity.Category;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.ReportSP;
import com.greenfarm.entity.User;

public interface ProductService {

	// Danh sách sản phẩm
	List<Product> findAll();
	
	List<Product> findAllDeletedProducts();
	
	List<Product> findByKeyword(String keyword);

	// Phân trang
	Page<Product> findAllByIsdeletedFalse(Pageable pageable);

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
	List<ReportSP> getTk_sp();

	// Thống kê lại sp
	List<Report> getTk_loai();

	// Thống kê sản phẩm tồn kho
	List<Product> getReportSpTk();


	List<ReportSP> getReportspbanchay();
	
//	void purchaseProduct(Integer productId, Integer quantityBought);
	
//	void purchaseProduct(ThongkeTK thongketk);
	
	List<Product> getProductsByCategory(Category category);

	void save(Product product);

	
}
