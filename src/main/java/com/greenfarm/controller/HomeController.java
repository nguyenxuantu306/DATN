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

    @RequestMapping("/tour-detail")
    public String ProductDetail(Model model){
        return "tour/detail";
    }

    @RequestMapping("/login")
    public String Login(Model model){
        return "login";
    }


    @RequestMapping("/register")
    public String Register(Model model){
        return "register";
    }

//    @RequestMapping({"/","/greenFarm.com"})
//	public String home() {
//		return "redirect:/greenFarm/tour";
//	}
//	
//	
//	@RequestMapping({"/admin","/admin/home/index"})
//	public String admin() {
//		return "redirect:/assets/admin/index.html";
//	}

}
