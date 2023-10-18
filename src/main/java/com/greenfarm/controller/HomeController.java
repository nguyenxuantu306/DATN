package com.greenfarm.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greenfarm.entity.Product;


@Controller
@RequestMapping("/")
public class HomeController {
    public String Home(Model model){
        return "index";
    }

    @RequestMapping("/profile")
    public String Profile(Model model) {
        return "profile";
    }
    
    @RequestMapping("/shop-list")
    public String ShopList(Model model) {
        return "product/shopList";
    }
    
    @RequestMapping("/tour")
    public String Tour(Model model) {
        return "tour/booking";
    }

    @RequestMapping("/tour-detail")
    public String TourDetail(Model model) {
        return "tour/detail";
    }

    @RequestMapping("/shop-detail")
    public String ShopDetail(Model model) {
        return "shop/detail";
    }

    @RequestMapping("/login")
    public String Login(Model model) {
        return "login";
    }

    @RequestMapping("/register")
    public String Register(Model model) {
        return "register";
    }

    @RequestMapping("/cart")
    public String Cart(Model model) {
        return "cart";
    }

	@RequestMapping({"/admin","/admin/home/index"})
	public String admin() {
		return "redirect:/assetsAdmin/admin/index.html";
	}

}
