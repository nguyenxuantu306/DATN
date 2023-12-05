package com.greenfarm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

import com.greenfarm.dto.AddressDTO;
import com.greenfarm.dto.CategoryDTO;
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

	
	@GetMapping("/address/{addressId}")
	public ResponseEntity<Address> getAddress(@PathVariable Integer addressId) {
		Address address = addressService.findById(addressId);
		if (address != null) {
			return new ResponseEntity<>(address, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	@PutMapping("/address/{addressId}")
	public ResponseEntity<AddressDTO> updateAddress(@PathVariable Integer addressId,
			@RequestBody Address updatedAddress) {

		Address updateAddress = addressService.update(updatedAddress);

		if (updateAddress == null) {
			// Trả về mã trạng thái 404 Not Found nếu không tìm thấy product để cập nhật
			return ResponseEntity.notFound().build();
		}

		AddressDTO addressDTO = modelMapper.map(updateAddress, AddressDTO.class);

		return new ResponseEntity<>(addressDTO, HttpStatus.OK);

//		Address address = addressService.findById(addressId);
//		if (address != null) {
//			// Cập nhật các trường của địa chỉ
//			address.setStreet(updatedAddress.getStreet());
//			address.setDistrict(updatedAddress.getDistrict());
//			address.setCity(updatedAddress.getCity());
//			address.setActive(updatedAddress.getActive());
//
//			// Lưu cập nhật địa chỉ
//			addressService.update(address);
//
//			return new Response	Entity<>(address, HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
	}

}

