package com.greenfarm.restcontroller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenfarm.dto.ProductDTO;
import com.greenfarm.entity.Product;
import com.greenfarm.service.ProductService;

@RestController
public class ProductRestController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/product/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword) {
        List<Product> productList = productService.findProductByKeyword(keyword);
        List<ProductDTO> productDTOList = productList.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOList);
    }

    @GetMapping("/product/filter")
    public ResponseEntity<List<ProductDTO>> filterProducts(
            @RequestParam("minPrice") Double minPrice,
            @RequestParam("maxPrice") Double maxPrice) {
        List<Product> productList = productService.findProductsByPriceRange(minPrice, maxPrice);
        List<ProductDTO> productDTOList = productList.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOList);
    }

    @GetMapping("/product/filter-by-custom-price-range")
    public ResponseEntity<List<ProductDTO>> filterByCustomPriceRange(
            @RequestParam("priceRange") String priceRange) {
        List<Product> productList = productService.findProductByPriceRange(priceRange);
        List<ProductDTO> productDTOList = productList.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOList);
    }

    @GetMapping("/product/sort")
    public ResponseEntity<List<ProductDTO>> sortProductsByName(@RequestParam("sort") String sort) {
        List<Product> productList = productService.findProductByProductNameSort(sort);
        List<ProductDTO> productDTOList = productList.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOList);
    }

    @GetMapping("/product/sortprice")
    public ResponseEntity<List<ProductDTO>> sortProductsByPrice(@RequestParam("sortprice") Integer sortprice) {
        List<Product> productList = productService.findProductByProductPiceSort(sortprice);
        List<ProductDTO> productDTOList = productList.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOList);
    }
}
