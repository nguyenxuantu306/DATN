package com.greenfarm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.github.sarxos.webcam.Webcam;

@Controller
public class SoatVeController {

	
	  @GetMapping("/soatve")
	    public String soatve(Model model) {
	    	System.out.println("uar");
	    	List<Webcam> webcams = Webcam.getWebcams();
	    	
	    	model.addAttribute("listcamera", webcams);
	    		System.out.println(webcams);

	    	// Chọn camera đầu tiên trong danh sách
	    	Webcam selectedWebcam = webcams.get(0);
	    	System.out.println(selectedWebcam);
	    	return "readqrcode";
	    }
}
