package com.greenfarm.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greenfarm.dto.ProductDTO;
import com.greenfarm.dto.UserDTO;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.Review;
import com.greenfarm.entity.User;
import com.greenfarm.service.ProductService;
import com.greenfarm.service.ReviewService;
import com.greenfarm.service.UserService;

import jakarta.validation.Valid;


@Controller
public class ProductController {
	@Autowired
	ProductService productService;
	
	@Autowired 
	ReviewService reviewService;

	@Autowired
	UserService userService;
	
	@Autowired
	ModelMapper modelMapper;

	// List All
	@GetMapping("/product/shop")
	public String shopList(Model model, @PageableDefault(size = 12) Pageable pageable,
			@RequestParam("cid") Optional<String> cid) {
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
	
	@GetMapping("/product/detail/{productid}")
	public String detail(Model model, @PathVariable("productid") Integer productid) {
		Integer id = productid;
	    Product item = productService.findById(id);
	    
	    if (item != null) {
	        ProductDTO itemDTO = modelMapper.map(item, ProductDTO.class);
	        model.addAttribute("item", itemDTO);
	    } else {
	        // Xử lý trường hợp đối tượng không tồn tại
	    }
	    
	    
	    List<Review> review = reviewService.findbyproduct(item);
	    model.addAttribute("review", review);
	    model.addAttribute("reviewinsert", new Review());

	    return "product/detail";
	}
	
	@PostMapping("/product/detail/{productid}")
	public String review(@PathVariable("productid") Integer productid,@ModelAttribute("reviewinsert") Review reviewinsert) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();		
		User user = userService.findByEmail(username);
		reviewinsert.setUser(user);
		Product product =  productService.findById(productid);
		reviewinsert.setProduct(product);
		// Lấy ngày giờ hiện tại
	    LocalDateTime currentDateTime = LocalDateTime.now();
	    reviewinsert.setDatepost(currentDateTime);
		reviewService.create(reviewinsert);
		
		return "redirect:/product/detail/" + productid;		
	}
	
	@GetMapping("/product/detail/{productid}/delete/{reviewid}")
	public String deleteReview(@PathVariable("productid") Integer productid, @PathVariable("reviewid") Integer reviewid) {
	    // Xác định logic xóa đánh giá dựa trên reviewid
	    boolean deleteResult = reviewService.deleteReviewById(reviewid);
	    
	    if (deleteResult) {
	        // Nếu xóa thành công, bạn có thể thực hiện chuyển hướng về trang chi tiết sản phẩm
	        return "redirect:/product/detail/" + productid;
	    } else {
	        // Xử lý trường hợp xóa không thành công, ví dụ: hiển thị thông báo lỗi
	        return "redirect:/product/detail/" + productid + "?error=delete_failed";
	    }
	}
	
}
