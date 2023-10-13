package com.greenfarm.controller;

import java.util.Optional;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


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

    @RequestMapping("/admin")
    public String Admin(Model model) {
        return "admin/homeAdmin";
    }

    @RequestMapping("/shop-management")
    public String ShopManagement(Model model) {
        return "admin/productManagement/shopManagement";
    }

    @RequestMapping("/tour-management")
    public String TourManagement(Model model) {
        return "admin/productManagement/tourManagement";
    }

    @RequestMapping("/edit-tour")
    public String EditTour(Model model) {
        return "admin/productManagement/editTour";
    }

    @RequestMapping("/edit-shop")
    public String EditShop(Model model) {
        return "admin/productManagement/editShop";
    }

    @RequestMapping("/inventory-statistics")
    public String InventoryStatistics(Model model) {
        return "admin/statistical/inventoryStatistics";
    }

    @RequestMapping("/revenue-statistics")
    public String RevenueStatistics(Model model) {
        return "admin/statistical/revenueStatistics";
    }
}
