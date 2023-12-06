package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.AddressDAO;
import com.greenfarm.entity.Address;

import com.greenfarm.service.AddressService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressServiceImpl implements AddressService {
	@Autowired
	AddressDAO addressDAO;
	@Autowired
	private HttpServletRequest request;

	@Override
	public List<Address> findAll() {
		return addressDAO.findAll();
	}

	@Override
	public Address findById(Integer addressid) {
		return addressDAO.findById(addressid).get();
	}

	@Override
	public Address create(Address address) {
		return addressDAO.save(address);
	}

	@Override
	public Address update(Address address) {
		return addressDAO.save(address);
	}

	@Override
	public void delete(Integer addressid) {
		addressDAO.deleteById(addressid);

	}

	@Override
	public List<Address> findByEfindByIdAccountmail(String email) {
		return addressDAO.findByEfindByIdAccountmail(email);
	}

	@Override
	@Transactional
	public void setActiveStatus(String email, Integer addressId) {
		// Cập nhật trạng thái Active của địa chỉ được chọn là true
		addressDAO.setActiveStatus(addressId);

		// Cập nhật trạng thái Active của các địa chỉ của người dùng đăng nhập còn lại
		// là false
		List<Address> userAddresses = addressDAO.findByEfindByIdAccountmail(email);
		for (Address address : userAddresses) {
			if (!address.getAddressID().equals(addressId)) {
				address.setActive(false);
				addressDAO.save(address);
			}
		}

	}

	@Override
	public Address updateById(Integer id, Address address) {
		// Kiểm tra xem address có tồn tại không
		Address existingAddress = addressDAO.findById(id).orElse(null);

		if (existingAddress != null) {
			// Thực hiện cập nhật các trường của existingAddress dựa trên address mới
			existingAddress.setStreet(address.getStreet());
			existingAddress.setDistrict(address.getDistrict());
			existingAddress.setCity(address.getCity());
			// Cập nhật thông tin user nếu cần
			existingAddress.setUser(address.getUser());

			// Lưu lại vào cơ sở dữ liệu
			return addressDAO.save(existingAddress);
		}

		return null;
	}

}
