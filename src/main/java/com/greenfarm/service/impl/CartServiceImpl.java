package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.CartDAO;
import com.greenfarm.entity.Cart;
import com.greenfarm.service.CartService;

@Service
public class CartServiceImpl implements CartService{
	@Autowired
	CartDAO dao;

	@Override
	public void delete(List<Cart> cartItems) {
		dao.deleteAll(cartItems);
		
	}
}
