package com.greenfarm.Controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.greenfarm.dto.TourDTO;
import com.greenfarm.entity.Tour;
import com.greenfarm.service.TourService;

@Controller
public class TourController {

	@Autowired
	TourService tourservice;

	@Autowired
	ModelMapper modelMapper;

	@RequestMapping("/tour")
	public String Tour(Model model) {
		List<Tour> list = tourservice.findAll();
		model.addAttribute("items", list);
		return "tour/tour";
	}
	
	
	@GetMapping("/tour/detail/{tourid}")
	public String detail(Model model, @PathVariable("tourid") Integer tourid) {
		Tour item = tourservice.findById(tourid);
	    
	    if (item != null) {
	        TourDTO itemDTO = modelMapper.map(item, TourDTO.class);
	        model.addAttribute("item", itemDTO);
	    } else {
	        // Xử lý trường hợp đối tượng không tồn tại
	    }

	    return "tour/detail";
	}
}
