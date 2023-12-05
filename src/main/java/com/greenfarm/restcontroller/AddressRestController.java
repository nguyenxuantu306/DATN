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

import com.greenfarm.dao.AddressDAO;
import com.greenfarm.dto.ProductDTO;
import com.greenfarm.entity.Address;
import com.greenfarm.entity.Product;
import com.greenfarm.service.AddressService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/addresses")
public class AddressRestController {

	@Autowired
	AddressService addressService;
	
	@Autowired
	ModelMapper modelMapper;
	
	
	@GetMapping()
	public ResponseEntity<List<AddressDAO>> getList() {
		List<Address> addresses = addressService.findAll();

		// Sử dụng ModelMapper để ánh xạ từ danh sách Product sang danh sách ProductDTO
		List<AddressDAO> addressDTOs = addresses.stream().map(address -> modelMapper.map(address, AddressDAO.class))
				.collect(Collectors.toList());

		// Trả về danh sách ProductDTO bằng ResponseEntity với mã trạng thái 200 OK
		return new ResponseEntity<>(addressDTOs, HttpStatus.OK);
	}

}
