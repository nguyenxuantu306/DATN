package com.greenfarm.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    public String Home(Model model) {
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

    @RequestMapping({ "/admin", "/admin/home/index" })
    public String admin() {
        return "redirect:/assetsAdmin/admin/index.html";
    }

}
