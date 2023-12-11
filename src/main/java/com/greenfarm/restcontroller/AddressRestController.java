package com.greenfarm.restcontroller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenfarm.dao.AddressDAO;
import com.greenfarm.dto.AddressDTO;
import com.greenfarm.dto.BookingDTO;
import com.greenfarm.dto.ProductDTO;
import com.greenfarm.entity.Address;
import com.greenfarm.entity.Booking;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.User;
import com.greenfarm.service.AddressService;
import com.greenfarm.service.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/addresses")
public class AddressRestController {

	@Autowired
	AddressService addressService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UserService userService;

	@GetMapping()
	public ResponseEntity<List<AddressDAO>> getList() {
		List<Address> addresses = addressService.findAll();

		// Sử dụng ModelMapper để ánh xạ từ danh sách Product sang danh sách ProductDTO
		List<AddressDAO> addressDTOs = addresses.stream().map(address -> modelMapper.map(address, AddressDAO.class))
				.collect(Collectors.toList());

		// Trả về danh sách ProductDTO bằng ResponseEntity với mã trạng thái 200 OK
		return new ResponseEntity<>(addressDTOs, HttpStatus.OK);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<AddressDTO> update(@PathVariable Integer id, @RequestBody Address address) {
		Address updatedAddress = addressService.updateById(id, address);

		if (updatedAddress == null) {
			return ResponseEntity.notFound().build();
		}

		AddressDTO addressDTO = modelMapper.map(updatedAddress, AddressDTO.class);
		return new ResponseEntity<>(addressDTO, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<AddressDTO> create(@RequestBody Address address) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = userService.findByEmail(userDetails.getUsername());
		address.setUser(user);
		Address createdAddress = addressService.create(address);

		if (createdAddress == null) {
			return ResponseEntity.notFound().build();
		}
		AddressDTO addressDTO = modelMapper.map(createdAddress, AddressDTO.class);
		return new ResponseEntity<>(addressDTO, HttpStatus.OK);
	}

}
