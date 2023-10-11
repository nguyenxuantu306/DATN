package com.greenfarm.controller;

import java.util.Comparator;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.greenfarm.dto.ProductDTO;
import com.greenfarm.entity.Product;
import com.greenfarm.service.ProductService;

@Controller
public class ProductController {
	@Autowired
	ProductService productService;

	@Autowired
	ModelMapper modelMapper;

	// List All	
	 @GetMapping("/product/shop")
		public String shopList(Model model, @PageableDefault(size = 9) Pageable pageable, @RequestParam("cid") Optional<String> cid) {
		    if (cid.isPresent()) {
		        List<Product> list = productService.findByCategoryId(cid.get());
		        model.addAttribute("items", list);
		    } else {
		        Page<Product> productsPage = productService.findAll(pageable);
		        Page<ProductDTO> productDTOPage = productsPage.map(product -> modelMapper.map(product, ProductDTO.class));
		        model.addAttribute("items", productDTOPage);
		    }

		    return "product/shop";
		}
	
	
	
	// Search Name
	@GetMapping("/product/search")
	public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam("keyword") Optional<String> keyword) {
		String searchKeyword = keyword.orElse("");
		List<Product> productList = productService.findProductByKeyword(searchKeyword);

		List<ProductDTO> productDTOList = productList.stream()
				.map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());

		return ResponseEntity.ok(productDTOList);
	}

	
	// Lọc theo giá 	
	@GetMapping("/product/filter")
    public ResponseEntity<List<ProductDTO>> filterProducts(
            @RequestParam("minPrice") Double minPrice,
            @RequestParam("maxPrice") Double maxPrice) {
        List<Product> productList = productService.findProductsByPriceRange(minPrice, maxPrice);

        List<ProductDTO> productDTOList = productList.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());

        return ResponseEntity.ok(productDTOList);
    }
		
	// Lọc theo khoảng giá
	@GetMapping("/product/filter-by-custom-price-range")
	public ResponseEntity<List<ProductDTO>> filterByCustomPriceRange(Model model, @RequestParam("priceRange") String priceRange) {
		List<Product> productList = productService.findProductByPriceRange(priceRange);

		List<ProductDTO> productDTOList = productList.stream()
				.map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());

		 return ResponseEntity.ok(productDTOList);
	}
	
	// Sắp xếp theo A - Z & Z - A
	 @GetMapping("/product/sort")
	    public ResponseEntity<List<ProductDTO>> sortProductsByName(@RequestParam("sort") String sort) {	        
	        List<Product> productList = productService.findProductByProductNameSort(sort);
	        List<ProductDTO> productDTOList = productList.stream()
	                .map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());

	        return ResponseEntity.ok(productDTOList);
	    }
}
