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

import com.greenfarm.dto.TourImageDTO;
import com.greenfarm.entity.TourImage;
import com.greenfarm.service.TourImageService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/tours")
public class TourImageRestController {
	@Autowired
	TourImageService tourImageService;

	@Autowired
	ModelMapper modelMapper;

	@GetMapping("/images")
	public ResponseEntity<List<TourImageDTO>> getList1() {
		List<TourImage> tourImages = tourImageService.findAll();
		List<TourImageDTO> tourImageDTOs = tourImages.stream()
				.map(tourimage -> modelMapper.map(tourimage, TourImageDTO.class)).collect(Collectors.toList());
		return new ResponseEntity<>(tourImageDTOs, HttpStatus.OK);
	}

//	@DeleteMapping("/image/{tourimageid}")
//	public ResponseEntity<Void> delete(@PathVariable("tourimageid") Integer tourimageid) {
//		tourImageService.delete(tourimageid);
//		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//	}
}
