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
	
	
	@GetMapping("/product/shop")
    public String shopList(Model model, @RequestParam("cid") Optional<Integer> cid) {
        List<Product> productList = productService.findAll();
        List<ProductDTO> productDTOList = productList.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

        model.addAttribute("items", productDTOList);

        return "product/shop";
    }
	
	@GetMapping("/product/search")
    public String searchProducts(Model model, @RequestParam("keyword") Optional<String> keyword) {
        String searchKeyword = keyword.orElse("");
        List<Product> productList = productService.findProductByKeyword(searchKeyword);

        List<ProductDTO> productDTOList = productList.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

        model.addAttribute("items", productDTOList);

        return "product/shop"; // Thay đổi tên trang HTML tương ứng
    }
	

	
	@GetMapping("/product/filter-by-price")
    public String filterByPrice(Model model,
                                @RequestParam("minPrice") Optional<Double> minPrice,
                                @RequestParam("maxPrice") Optional<Double> maxPrice
                                ) {
        List<Product> productList = productService.findProductByPriceRange(minPrice.orElse(null), maxPrice.orElse(null));

        List<ProductDTO> productDTOList = productList.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

        model.addAttribute("items", productDTOList);

        return "product/shop"; // Thay đổi tên trang HTML tương ứng
    }


	 @GetMapping("/product/filter-by-custom-price-range")
	    public String filterByCustomPriceRange(Model model, @RequestParam("priceRange") String priceRange) {
	        List<Product> productList = productService.findProductByPriceRange(priceRange);

	        List<ProductDTO> productDTOList = productList.stream()
	                .map(product -> modelMapper.map(product, ProductDTO.class))
	                .collect(Collectors.toList());

	        model.addAttribute("items", productDTOList);

	        return "product/shop"; // Thay đổi tên trang HTML tương ứng
	    }
	

	 @GetMapping("/product/filter-by-name")
	    public String filterByName(Model model, @RequestParam("sort") Optional<String> sort) {
	        List<Product> productList = productService.findProductByProductNameSort(sort.orElse(null));

	        List<ProductDTO> productDTOList = productList.stream()
	                .map(product -> modelMapper.map(product, ProductDTO.class))
	                .collect(Collectors.toList());

	        model.addAttribute("items", productDTOList);

	        return "product/shop"; // Thay đổi tên trang HTML tương ứng
	    }
}
