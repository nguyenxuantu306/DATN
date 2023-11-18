package com.greenfarm.service.impl;

import java.util.Comparator;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.greenfarm.dao.OrderDetailDAO;
import com.greenfarm.dao.ProductsDAO;
import com.greenfarm.entity.Category;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.ThongkeTK;
import com.greenfarm.entity.Top10;
import com.greenfarm.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductsDAO dao;

	@Autowired
	OrderDetailDAO orderdao;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public List<Product> findAll() {
		return dao.findAllByIsdeletedFalse();
	}

	@Override
	public List<Product> findAllDeletedProducts() {
		return dao.findAllByIsdeletedTrue();
	}

	public Page<Product> findAll(Pageable pageable) {
		return dao.findAll(pageable);
	}

	@Override
	public Product findById(Integer id) {
		return dao.findById(id).get();
	}

	@Override
	public Product create(Product Product) {
		return dao.save(Product);
	}

	@Override
	public Product update(Product Product) {
		return dao.save(Product);
	}

	@Override
	public void delete(Integer id) {
		dao.deleteByIsDeleted(id);
	}

	@Override
	public List<Product> findByCategoryId(String cid) {
		return dao.findByCategoryId(cid);
	}

	@Override
	public List<Product> findProductByKeyword(String keyword) {
		List<Product> productList;

		if (keyword != null && !keyword.isEmpty()) {
			productList = dao.findProductByKeyword(keyword);
		} else {
			productList = dao.findAll();
		}

		return productList;
	}

	@Override
	public List<Product> findProductByPriceRange(String priceRange) {
		List<Product> productList;

		if ("under30k".equals(priceRange)) {
			productList = dao.findByPriceBetween(0.0, 30000.0);
		} else if ("30kTo70k".equals(priceRange)) {
			productList = dao.findByPriceBetween(30000.0, 70000.0);
		} else if ("70kTo100k".equals(priceRange)) {
			productList = dao.findByPriceBetween(70000.0, 100000.0);
		} else if ("100kTo150k".equals(priceRange)) {
			productList = dao.findByPriceBetween(100000.0, 150000.0);
		} else if ("over150k".equals(priceRange)) {
			productList = dao.findByPriceBetween(150000.0, 99900000.0);
		} else {
			// Hiển thị tất cả sản phẩm nếu không có lựa chọn nào được chọn
			productList = dao.findAll();
		}

		return productList;
	}

	@Override
	public List<Product> findProductByProductNameSort(String sort) {
		List<Product> productList = dao.findAll();

		// Sắp xếp sản phẩm theo tên sản phẩm (A-Z hoặc Z-A)
		if (sort != null && sort.equalsIgnoreCase("desc")) {
			productList.sort(Comparator.comparing(Product::getProductname).reversed());
		} else {
			productList.sort(Comparator.comparing(Product::getProductname));
		}

		return productList;
	}

	@Override
	public List<Product> findProductByProductPiceSort(Integer sortprice) {
		List<Product> productList = dao.findAll();

		// Sắp xếp sản phẩm theo giá (tăng dần hoặc giảm dần)
		if (sortprice != null) {
			if (sortprice == 1) { // 1 có thể tượng trưng cho sắp xếp tăng dần
				productList.sort(Comparator.comparing(Product::getPrice));
			} else if (sortprice == 2) { // 2 có thể tượng trưng cho sắp xếp giảm dần
				productList.sort(Comparator.comparing(Product::getPrice).reversed());
			}
		}

		return productList;
	}

	@Override
	public List<Report> getTk_sp() {
		return dao.reportTheoProduct();
	}

	@Override
	public List<Report> getTk_loai() {
		return dao.getInventoryByCategory();
	}

	@Override
	public List<Product> getReportSpTk() {
		List<Product> productsByQuantityAvailable = dao.getTop10ProductsByQuantityAvailable();
		if (productsByQuantityAvailable.size() > 10) {
			return productsByQuantityAvailable.subList(0, 10);
		} else {
			return productsByQuantityAvailable;
		}
	}

	@Override
	public List<Report> getReportspbanchay() {
		List<Report> productsBygetReportspbanchay = dao.getTop10ProductsBygetReportspbanchay();
		if (productsBygetReportspbanchay.size() > 10) {
			return productsBygetReportspbanchay.subList(0, 10);
		} else {
			return productsBygetReportspbanchay;
		}
	}
//	@Transactional
//    @Override
//    public void purchaseProduct(Integer productId, Integer quantityBought) {
//        dao.updateProductQuantity(productId, quantityBought);
//    }

//	@Override
//	public void purchaseProduct(ThongkeTK thongketk) {
//		dao.updateProductQuantity(thongketk.getProductid(),thongketk.getQuantitybought());
//		
//	}
	@Override
	public List<Product> getProductsByCategory(Category category) {
		// TODO Auto-generated method stub
		return dao.getProductsByCategory(category);
	}

	@Override
	public void save(Product product) {
	    dao.save(product);
	}

}
