package com.greenfarm.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	                   @RequestParam(name = "page", defaultValue = "1") int page,
	                   @RequestParam(name = "size", defaultValue = "6") Integer size,
	                   Model model) {
		Pageable pageable = PageRequest.of(page - 1, size);

        if (priceSpread != null && !priceSpread.isEmpty()) {
            String[] priceRange = priceSpread.split("-");
            if (priceRange.length == 2) {
                Float minPrice = Float.parseFloat(priceRange[0]);
                Float maxPrice = Float.parseFloat(priceRange[1]);
                Page<TourDTO> tourPage = tourservice.findToursByAdultPriceWithPagination(minPrice, maxPrice, pageable);
                model.addAttribute("tourDTOs", tourPage.getContent());
                model.addAttribute("totalPages", tourPage.getTotalPages());
                model.addAttribute("currentPage", page);
            }
        } else if (searchTerm != null && !searchTerm.isEmpty()) {
            Page<TourDTO> tourPage = tourservice.findToursByTournameWithPagination(searchTerm, pageable);
            model.addAttribute("tourDTOs", tourPage.getContent());
            model.addAttribute("totalPages", tourPage.getTotalPages());
            model.addAttribute("currentPage", page);
        } else {
            Page<Tour> tourPage = tourservice.findAllWithPagination(pageable);
            model.addAttribute("items", tourPage.getContent());
            model.addAttribute("totalPages", tourPage.getTotalPages());
            model.addAttribute("currentPage", page);
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
