package com.greenfarm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.greenfarm.dao.CartDAO;
import com.greenfarm.entity.Cart;
import com.greenfarm.entity.Top10;
import com.greenfarm.entity.Top3;
import com.greenfarm.service.BookingService;
import com.greenfarm.service.OrderDetailService;
import com.greenfarm.service.TourService;

@Controller
@RequestMapping("/")
public class HomeController {
	@Autowired
	OrderDetailService orderDetailService;

	@Autowired
	BookingService bookingService;

	@Autowired
	TourService tourService;

	@Autowired
	CartDAO cartDAO;

	@RequestMapping("")
	public String Home(Model model) {
		Pageable pageable = PageRequest.of(0, 8);
		Pageable pageable1 = PageRequest.of(0, 3);
		Page<Top10> topList = orderDetailService.getTop10(pageable);
		Page<Top3> topTour = bookingService.getTop3Tour(pageable1);

		model.addAttribute("topList", topList);
		model.addAttribute("topTour", topTour);

		return "user/index";
	}

	@RequestMapping("/checkLoginStatus")
	public String checkLoginStatus(Authentication authentication) {
	    if (authentication != null && authentication.isAuthenticated()) {
	        // Đã đăng nhập
	        return "User is logged in";
	    } else {
	        // Chưa đăng nhập
	        return  "User is not logged in";
	    }
	}

	
	@RequestMapping("/userInfo")
	public String getUserInfo(Authentication authentication) {
	    if (authentication != null && authentication.isAuthenticated()) {
	        // Lấy thông tin người dùng từ đối tượng Authentication
//	        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//	        
//	        // Lấy thông tin cụ thể từ OAuth2User
//	        String email = oAuth2User.getAttribute("email");
	        // Thực hiện các xử lý khác với thông tin người dùng
	        
	        return "User is logged in. Email: " + authentication.getPrincipal();
	    } else {
	        return "User is not logged in";
	    }
	}


	
	@RequestMapping("/success")
	public String Success(ModelMap modelMap) {
		return "success";
	}

//	@RequestMapping("/profile")
//	public String Profile(Model model) {
//		return "profile";
//	}


	@RequestMapping("/contact")
	public String Contact(Model model) {
		return "contact";
	}

//	@RequestMapping("/login23")
//	public String Contact1(Model model) {
//		return "security/register";
//	}

	@RequestMapping("/about")
	public String foodter(Model model) {
		return "about";
	}

	@RequestMapping({ "/admin", "/admin/home/index" })
	public String admin() {
		return "redirect:/assetsAdmin/admin/index.html";
	}

	public double totalPrice(List<Cart> cartItems) {
		double total = 0;
		for (Cart cartItem : cartItems) {
			total += cartItem.getProduct().getPrice() * cartItem.getQuantity();
		}
		return total;
	}

}
