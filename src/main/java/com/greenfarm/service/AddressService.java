package com.greenfarm.service;

import java.util.List;
import java.util.Optional;

import com.greenfarm.entity.Address;
import com.greenfarm.entity.Order;

public interface AddressService {
	List<Address> findAll();

	// tìm sản phẩm theo id
	Address findById(Integer addressid);

	// thêm sản phẩm
	Address create(Address address);

	// cập nhật sản phẩm
	Address update(Address Address);

	// xóa sản phẩm
	void delete(Integer addressid);

	List<Address> findByEfindByIdAccountmail(String email);
	
	
	void setActiveStatus(String email,Integer addressId);
	

}
