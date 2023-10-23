package com.greenfarm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TourController {

	   @RequestMapping("/tour")
	    public String Tour(Model model) {
		   
	        return "tour/booking";
	    }
}
