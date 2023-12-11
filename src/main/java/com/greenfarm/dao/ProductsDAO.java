package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.greenfarm.entity.Category;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.ReportSP;

public interface ProductsDAO extends JpaRepository<Product, Integer> {

	@Query("SELECT p FROM Product p WHERE p.productname like %?1%")
	List<Product> findProductByKeyword(String keyword);

	@Query("SELECT p FROM Product p WHERE p.price BETWEEN ?1 AND ?2")
	List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

	@Query("SELECT p FROM Product p WHERE p.category.categoryid=?1 AND p.isdeleted = false")
	List<Product> findByCategoryIdAndIsdeletedFalse(String cid);

	@Query("SELECT new ReportSP(o.product, sum(o.totalprice * o.quantityordered), sum(o.quantityordered)) FROM OrderDetail o GROUP BY o.product ORDER BY sum(o.totalprice * o.quantityordered)")
	List<ReportSP> reportTheoProduct();

	@Query("SELECT new ReportSP(o.category, sum(o.price), count(o)) FROM Product o GROUP BY o.category ORDER BY sum(o.price) DESC")
	List<ReportSP> getInventoryByCategory();

	@Query("SELECT new Report(o.product, sum(o.totalprice * o.quantityordered), sum(o.quantityordered)) FROM OrderDetail o GROUP BY o.product ORDER BY sum(o.quantityordered) DESC")
	List<Report> getTop10ProductsBygetReportspbanchay();

	@Query("SELECT p FROM Product p WHERE p.category = :category")
	List<Product> getProductsByCategory(@Param("category") Category category);

	@Query("SELECT p FROM Product p JOIN p.orderDetails od GROUP BY p.productid, p.productname,p.image ORDER BY SUM(od.quantityordered) DESC")
	List<Product> findTopSellingProducts();

	@Query("SELECT p FROM Product p ORDER BY p.quantityavailable DESC")
	List<Product> getTop10ProductsByQuantityAvailable();

	@Query("SELECT p FROM Product p WHERE p.isdeleted = true")
	List<Product> findAllDeletedProducts();

	@Query("SELECT p FROM Product p WHERE p.productid = :id")
	Product findProductById(@Param("id") Integer id);

	default void deleteByIsDeleted(Integer id) {
		Product product = findProductById(id);
		if (product != null) {
			product.setIsDeleted(true);
			save(product);
		}
	}

	List<Product> findAllByIsdeletedFalse();

	List<Product> findAllByIsdeletedTrue();

	Page<Product> findAllByIsdeletedFalse(Pageable pageable);

	// // Phương thức tùy chỉnh để tìm sản phẩm theo productId và cập nhật số lượng
	// @Modifying
	// @Transactional
	// @Query("UPDATE Product p SET p.quantityavailable = p.quantityavailable -
	// :quantityBought WHERE p.productid = :productId")
	// void updateProductQuantity(@Param("productId") Integer productId,
	// @Param("quantityBought") Integer quantityBought);

}
