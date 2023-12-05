package com.greenfarm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.transaction.annotation.Transactional;

import com.greenfarm.entity.Address;
import com.greenfarm.entity.User;
import com.greenfarm.service.AddressService;
import com.greenfarm.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AddressController {
	@Autowired
	AddressService addressService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

	@RequestMapping("/address")
	public String list(Model model, HttpServletRequest request) {
		String email = request.getRemoteUser();
		model.addAttribute("addresses", addressService.findByEfindByIdAccountmail(email));
		return "address";
	}

	@PostMapping("/updateAddressStatus/{addressId}")
	@ResponseBody
	public String updateAddressStatus(@PathVariable("addressId") Integer addressId, HttpServletRequest request) {
		String email = request.getRemoteUser();
		addressService.setActiveStatus(email, addressId);
		return "Success";
	}

	@GetMapping("/getAddress/{addressId}")
	@ResponseBody
	public Map<String, Object> getAddress(@PathVariable("addressId") Integer addressId) {
		Map<String, Object> response = new HashMap<>();

		try {
			Address address = addressService.findById(addressId);
			User user = userService.findById(address.getUser().getUserid());

			response.put("address", address);
			response.put("user", user);
			response.put("success", true);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "Error getting address information: " + e.getMessage());
		}

		return response;
	}
}
