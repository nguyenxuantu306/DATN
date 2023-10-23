package com.greenfarm.restcontroller;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.greenfarm.dto.TourDTO;
import com.greenfarm.entity.Tour;
import com.greenfarm.service.TourService;


@CrossOrigin("*")
@RestController
@RequestMapping("/rest/tours")
public class TourRestController {

	@Autowired
	TourService tourService;

	@Autowired
	ModelMapper modelMapper;

	@GetMapping()
	public ResponseEntity<List<TourDTO>> getList() {
	    List<Tour> tours = tourService.findAll();
	    List<TourDTO> tourDTOs = tours.stream()
	            .map(tour -> modelMapper.map(tour, TourDTO.class))
	            .collect(Collectors.toList());
	    return new ResponseEntity<>(tourDTOs, HttpStatus.OK);
	}
}