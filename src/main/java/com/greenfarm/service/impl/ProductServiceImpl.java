package com.greenfarm.service.impl;

import java.util.Comparator;
import java.util.List;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.util.ArrayBuilders.BooleanBuilder;
import com.greenfarm.dao.ProductsDAO;
import com.greenfarm.entity.Product;
import com.greenfarm.service.ProductService;




@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductsDAO dao;

	@Override
	public List<Product> findAll() {
		return dao.findAll();
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
		dao.deleteById(id);
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


//	@Override
//    public List<Product> findByPriceBetween(Double minPrice, Double maxPrice) {
//        return dao.findByPriceBetween(minPrice, maxPrice);
//    }

	@Override
    public List<Product> findProductsByPriceRange(Double minPrice, Double maxPrice) {
        List<Product> productList;

        if (minPrice != null && maxPrice != null) {
            // Thực hiện lọc sản phẩm theo giá từ minPrice đến maxPrice
            productList = dao.findByPriceBetween(minPrice, maxPrice);
        } else {
            // Hiển thị tất cả sản phẩm nếu không có giá trị minPrice và maxPrice
            productList = dao.findAll();
        }

        return productList;
    }

	@Override
    public List<Product> findProductByPriceRange(String priceRange) {
        List<Product> productList;

    if ("under500k".equals(priceRange)) {
    	productList = dao.findByPriceBetween(0.0 , 50.0);
    } else if ("500kTo1M".equals(priceRange)) {
        productList = dao.findByPriceBetween(50.0, 100.0);
    } else if ("1MTo1.5M".equals(priceRange)) {
        productList = dao.findByPriceBetween(100.0, 150.0);
    } else if ("2MTo5M".equals(priceRange)) {
        productList = dao.findByPriceBetween(200.0, 500.0);
    } else if ("over5M".equals(priceRange)) {
        productList = dao.findByPriceBetween(500.0, Double.MAX_VALUE);
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

	
}
