package com.greenfarm.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.greenfarm.dto.ProductDTO;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.Review;
import com.greenfarm.entity.User;
import com.greenfarm.service.ProductService;
import com.greenfarm.service.ReviewService;
import com.greenfarm.service.UserService;


@Controller
public class ProductController {


	@Autowired
	ReviewService reviewService;

	@Autowired
	UserService userService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	ProductService productService;

	// List All
	@GetMapping("/product/shop")
	public String shopList(Model model, @PageableDefault(size = 9) Pageable pageable,
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

//	@PostMapping("/product/detail/{productid}")
//	public String review(Model model,@PathVariable("productid") Integer productid,@ModelAttribute("reviewinsert") Review reviewinsert) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		String username = authentication.getName();		
//		User user = userService.findByEmail(username);
//		reviewinsert.setUser(user);
//		Product product =  productService.findById(productid);
//		reviewinsert.setProduct(product);
//		// Lấy ngày giờ hiện tại
//	    LocalDateTime currentDateTime = LocalDateTime.now();
//	    reviewinsert.setDatepost(currentDateTime);
//		reviewService.create(reviewinsert);
//		model.addAttribute("successMessage", "Đánh giá của bạn đã được lưu thành công.");
//
//		return "redirect:/product/detail/" + productid;		
//	}
//	@PostMapping("/product/detail/{productid}")
//	public String review(Model model, @PathVariable("productid") Integer productid,
//			@ModelAttribute("reviewinsert") Review reviewinsert) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		String username = authentication.getName();
//		User user = userService.findByEmail(username);
//		Product product = productService.findById(productid);
//
//		// Kiểm tra xem tài khoản đã đánh giá sản phẩm này chưa
//		boolean hasUserReviewedProduct = reviewService.hasUserReviewedProduct(user, product);
//
//		if (hasUserReviewedProduct) {
//			// Nếu đã đánh giá rồi, hiển thị thông báo lỗi và không lưu đánh giá mới
//			model.addAttribute("errorMessage", "Bạn đã đánh giá sản phẩm này rồi.");
//		} else {
//			// Nếu chưa đánh giá, lưu đánh giá mới và hiển thị thông báo thành công
//			LocalDateTime currentDateTime = LocalDateTime.now();
//			reviewinsert.setUser(user);
//			reviewinsert.setProduct(product);
//			reviewinsert.setDatepost(currentDateTime);
//			reviewService.create(reviewinsert);
//			model.addAttribute("successMessage", "Đánh giá của bạn đã được lưu thành công.");
//		}
//
//		return "redirect:/product/detail/" + productid;
//	}

	@PostMapping("/product/detail/{productid}")
	public ResponseEntity<String> review(Model model, @PathVariable("productid") Integer productid,
			@ModelAttribute("reviewinsert") Review reviewinsert) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		User user = userService.findByEmail(username);
		Product product = productService.findById(productid);

		// Kiểm tra xem tài khoản đã đánh giá sản phẩm này chưa
		boolean hasUserReviewedProduct = reviewService.hasUserReviewedProduct(user, product);

		if (hasUserReviewedProduct) {
			  // Nếu đã đánh giá rồi, gửi thông báo lỗi
	        ObjectMapper mapper = new ObjectMapper();
	        ObjectNode responseJson = mapper.createObjectNode();
	        responseJson.put("errorMessage", "Bạn đã đánh giá sản phẩm này rồi.");
	        return ResponseEntity.badRequest().body(responseJson.toString());
		} else {
			// Nếu chưa đánh giá, lưu đánh giá mới
			LocalDateTime currentDateTime = LocalDateTime.now();
			reviewinsert.setUser(user);
			reviewinsert.setProduct(product);
			reviewinsert.setDatepost(currentDateTime);
			reviewService.create(reviewinsert);

			// Gửi thông báo thành công dưới dạng JSON
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode responseJson = mapper.createObjectNode();
			responseJson.put("successMessage", "Đánh giá của bạn đã được lưu thành công.");
			return ResponseEntity.ok().body(responseJson.toString());
		}
	}

	@GetMapping("/product/detail/{productid}/delete/{reviewid}")
	public String deleteReview(@PathVariable("productid") Integer productid,
			@PathVariable("reviewid") Integer reviewid) {
		// Xác định logic xóa đánh giá dựa trên reviewid
		boolean deleteResult = reviewService.deleteReviewById(reviewid);

		if (deleteResult) {
			// Nếu xóa thành công, bạn có thể thực hiện chuyển hướng về trang chi tiết sản
			// phẩm
			return "redirect:/product/detail/" + productid;
		} else {
			// Xử lý trường hợp xóa không thành công, ví dụ: hiển thị thông báo lỗi
			return "redirect:/product/detail/" + productid + "?error=delete_failed";
		}
	}

}
