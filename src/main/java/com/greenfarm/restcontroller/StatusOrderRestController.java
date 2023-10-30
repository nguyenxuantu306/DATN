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

import com.greenfarm.dto.StatusOrderDTO;
import com.greenfarm.entity.StatusOrder;
import com.greenfarm.service.StatusOrderService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/statusorder")
public class StatusOrderRestController {

	@Autowired
	StatusOrderService statusService;

	@Autowired
	ModelMapper modelMapper;

	@GetMapping()
	public ResponseEntity<List<StatusOrderDTO>> getList() {
		List<StatusOrder> statusOrder = statusService.findAll();

		// Sử dụng modelMapper để ánh xạ danh sách Category sang danh sách CategoryDTO
		ModelMapper modelMapper = new ModelMapper();
		List<StatusOrderDTO> statusOrderDtos = statusOrder.stream()
				.map(statusorder -> modelMapper.map(statusorder, StatusOrderDTO.class)).collect(Collectors.toList());

		return ResponseEntity.ok(statusOrderDtos); // ResponseEntity.ok() tương đương HttpStatus.OK
	}

}
