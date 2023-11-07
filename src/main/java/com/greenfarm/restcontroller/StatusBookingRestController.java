package com.greenfarm.restcontroller;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.greenfarm.dto.StatusBookingDTO;
import com.greenfarm.entity.StatusBooking;
import com.greenfarm.service.StatusBookingService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/statusbooking")
public class StatusBookingRestController {
	@Autowired
	StatusBookingService statusBookingService;

	@Autowired
	ModelMapper modelMapper;

	@GetMapping()
	public ResponseEntity<List<StatusBookingDTO>> getList() {
		List<StatusBooking> statusBooking = statusBookingService.findAll();

		ModelMapper modelMapper = new ModelMapper();
		List<StatusBookingDTO> statusBookingDtos = statusBooking.stream()
				.map(statusbooking -> modelMapper.map(statusbooking, StatusBookingDTO.class)).collect(Collectors.toList());

		return ResponseEntity.ok(statusBookingDtos);
	}
}
