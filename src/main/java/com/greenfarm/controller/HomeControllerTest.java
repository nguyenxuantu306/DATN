package com.greenfarm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")

public class HomeControllerTest {
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

    

}