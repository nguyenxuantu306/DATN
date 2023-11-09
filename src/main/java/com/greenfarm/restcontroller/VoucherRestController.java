package com.greenfarm.restcontroller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenfarm.dto.OrderDTO;
import com.greenfarm.dto.ProductDTO;
import com.greenfarm.dto.VoucherDTO;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.Voucher;
import com.greenfarm.service.VoucherService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/vouchers")
public class VoucherRestController {
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	VoucherService voucherService;
	
	@GetMapping()
	public ResponseEntity<List<VoucherDTO>> getList() {
		List<Voucher> vouchers = voucherService.findAll();
		List<VoucherDTO> voucherDTOs = vouchers.stream().map(voucher -> modelMapper.map(voucher, VoucherDTO.class))
				.collect(Collectors.toList());
		return new ResponseEntity<>(voucherDTOs, HttpStatus.OK);
	}
	
	@GetMapping("{voucherid}")
	public ResponseEntity<VoucherDTO> getOne(@PathVariable("voucherid") Integer voucherid) {
		Voucher voucher = voucherService.findById(voucherid);
		if (voucher == null) {
			return ResponseEntity.notFound().build();
		}
		VoucherDTO voucherDTO = modelMapper.map(voucher, VoucherDTO.class);
		return new ResponseEntity<>(voucherDTO, HttpStatus.OK);
	}
	
	@PostMapping()
	public ResponseEntity<VoucherDTO> create(@RequestBody Voucher voucher, Model model) {

		Voucher createdVoucher = voucherService.create(voucher);
		VoucherDTO voucherDTO = modelMapper.map(createdVoucher, VoucherDTO.class);
		return new ResponseEntity<>(voucherDTO, HttpStatus.CREATED);
	}
	
	@PutMapping("{voucherid}")
	public ResponseEntity<VoucherDTO> update(@PathVariable("id") Integer voucherid, @RequestBody Voucher voucher) {
		Voucher updatedVoucher = voucherService.update(voucher);

		if (updatedVoucher == null) {
			return ResponseEntity.notFound().build();
		}
		VoucherDTO voucherDTO = modelMapper.map(updatedVoucher, VoucherDTO.class);
		return new ResponseEntity<>(voucherDTO, HttpStatus.OK);
	}
	
	@DeleteMapping("{voucherid}")
	public ResponseEntity<Void> delete(@PathVariable("voucherid") Integer voucherid) {
		Voucher existingVoucher = voucherService.findById(voucherid);

		if (existingVoucher == null) {
			return ResponseEntity.notFound().build();
		}
		voucherService.delete(voucherid);
		return ResponseEntity.noContent().build();
	}

	
	
}
