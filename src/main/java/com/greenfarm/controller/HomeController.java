package com.greenfarm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

	@RequestMapping("/paypal")
	public String Paypal(Model model) {
		return "paypal";
	}
	
	@RequestMapping("/success")
	public String Success(Model model) {
		return "success";
	}

	@RequestMapping("/profile")
	public String Profile(Model model) {
		return "profile";
	}

	@RequestMapping("/shop-list")
	public String ShopList(Model model) {
		return "product/shopList";
	}

	@RequestMapping("/tour-detail")
	public String TourDetail(Model model) {
		return "tour/detail";
	}

//
//    @RequestMapping("/register")
//    public String Register(Model model) {
//        return "register";
//    }

//    @RequestMapping("/cart")
//    public String Cart(Model model) {
//        return "cart";
//    }

	@RequestMapping({ "/admin", "/admin/home/index" })
	public String admin() {
		return "redirect:/assetsAdmin/admin/index.html";
	}

}
