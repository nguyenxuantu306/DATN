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

import com.greenfarm.dto.PaymentMethodDTO;
import com.greenfarm.dto.UserDTO;
import com.greenfarm.entity.PaymentMethod;
import com.greenfarm.entity.User;
import com.greenfarm.service.PaymentMethodService;
import com.greenfarm.service.UserService;


@CrossOrigin("*")
@RestController
@RequestMapping("/rest/paymentmethods")
public class PaymentMethodRestController {
	@Autowired
	PaymentMethodService paymentMethodService;

	@Autowired
	ModelMapper modelMapper;

	@GetMapping()
	public ResponseEntity<List<PaymentMethodDTO>> getList() {
		List<PaymentMethod> paymentMethods = paymentMethodService.findAll();

		ModelMapper modelMapper = new ModelMapper();
		List<PaymentMethodDTO> paymentMethodDtos = paymentMethods.stream().map(paymentMethod -> modelMapper.map(paymentMethod, PaymentMethodDTO.class))
				.collect(Collectors.toList());

		return ResponseEntity.ok(paymentMethodDtos); 
	}

}
