package com.greenfarm.controller;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.greenfarm.dto.ProductDTO;
import com.greenfarm.entity.Product;
import com.greenfarm.service.ProductService;

@Configuration
@Controller
public class ProductController {
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Autowired
	ProductService productService;

	// List All
	@GetMapping("/product/shop")
	public String shopList(Model model, @PageableDefault(size = 12) Pageable pageable,
			@RequestParam("cid") Optional<String> cid) {
		if (cid.isPresent()) {
			List<Product> list = productService.findByCategoryId(cid.get());
			model.addAttribute("items", list);
		} else {
			Page<Product> productsPage = productService.findAll(pageable);
			Page<ProductDTO> productDTOPage = productsPage.map(product -> modelMapper().map(product, ProductDTO.class));
			model.addAttribute("items", productDTOPage);
		}

		return "product/shop";
	}
	
	@GetMapping("/product/detail/{productid}")
	public String detail(Model model, @PathVariable("productid") Integer productid) {
	    Product item = productService.findById(productid);
	    
	    if (item != null) {
	        ProductDTO itemDTO = modelMapper().map(item, ProductDTO.class);
	        model.addAttribute("item", itemDTO);
	    } else {
	        // Xử lý trường hợp đối tượng không tồn tại
	    }

	    return "product/detail";
	}
	
	
}
