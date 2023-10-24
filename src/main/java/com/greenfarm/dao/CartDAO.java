package com.greenfarm.dao;

import java.util.HashMap;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.Cart;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.User;

public interface CartDAO extends JpaRepository<Cart, Integer>{
    Cart findByUserAndProduct(User user, Product product);

    HashMap<Integer, Cart> getCartItemsByUser(User user);
}
