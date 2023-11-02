package com.greenfarm.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String Tour(@RequestParam(name = "price_spread", required = false) String priceSpread,
	                   @RequestParam(name = "search", required = false) String searchTerm,
	                   Model model) {
	    if (priceSpread != null && !priceSpread.isEmpty()) {
	        String[] priceRange = priceSpread.split("-");
	        if (priceRange.length == 2) {
	            Float minPrice = Float.parseFloat(priceRange[0]);
	            Float maxPrice = Float.parseFloat(priceRange[1]);
	            List<TourDTO> tourDTOs = tourservice.findToursByAdultPrice(minPrice, maxPrice);
	            model.addAttribute("tourDTOs", tourDTOs);
	        }
	    } else if (searchTerm != null && !searchTerm.isEmpty()) {
	        List<TourDTO> tourDTOs = tourservice.findToursByTourname(searchTerm);
	        model.addAttribute("tourDTOs", tourDTOs);
	    } else {
	        List<Tour> list = tourservice.findAll();
	        model.addAttribute("items", list);
	    }
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
