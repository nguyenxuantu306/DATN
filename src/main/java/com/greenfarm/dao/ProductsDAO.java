package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.greenfarm.entity.Product;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.Top10;

public interface ProductsDAO extends JpaRepository<Product, Integer> {

	@Query("SELECT p FROM Product p WHERE p.productname like %?1%")
	List<Product> findProductByKeyword(String keyword);

	@Query("SELECT p FROM Product p WHERE p.price BETWEEN ?1 AND ?2")
	List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

	@Query("SELECT p FROM Product p WHERE p.category.categoryid=?1")
	List<Product> findByCategoryId(String cid);

	@Query("SELECT new Report(o.product, sum(o.totalprice * o.quantityordered),sum(o.quantityordered))FROM OrderDetail o "
			+ " GROUP BY o.product" + " ORDER BY  sum(o.totalprice * o.quantityordered)")
	List<Report> reportTheoProduct();

	@Query("SELECT new Report(o.category, sum(o.price), count(o)) " + " FROM Product o " + " GROUP BY o.category"
			+ " ORDER BY sum(o.price) DESC")
	List<Report> getInventoryByCategory();

	@Query("SELECT p FROM Product p JOIN p.orderDetails od GROUP BY p.productid, p.productname,p.image ORDER BY SUM(od.quantityordered) DESC")
	List<Product> findTopSellingProducts();

	@Query("SELECT p FROM Product p ORDER BY p.quantityavailable DESC")
	List<Product> getTop10ProductsByQuantityAvailable();

	@Query("SELECT new Report(o.product, sum(o.totalprice * o.quantityordered),sum(o.quantityordered))FROM OrderDetail o "
			+ " GROUP BY o.product" + " ORDER BY  sum(o.quantityordered) Desc")
	List<Report> getTop10ProductsBygetReportspbanchay();
	
	
//	// Phương thức tùy chỉnh để tìm sản phẩm theo productId và cập nhật số lượng
// 	@Modifying
//    @Transactional
//    @Query("UPDATE Product p SET p.quantityavailable = p.quantityavailable - :quantityBought WHERE p.productid = :productId")
//    void updateProductQuantity(@Param("productId") Integer productId, @Param("quantityBought") Integer quantityBought);
	
}
