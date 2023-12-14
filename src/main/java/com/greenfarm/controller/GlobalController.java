package com.greenfarm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;

import com.greenfarm.dao.CartDAO;
import com.greenfarm.entity.Cart;
import com.greenfarm.entity.User;
import com.greenfarm.service.UserService;

@ControllerAdvice
public class GlobalController {

	@Autowired
	UserService userService;

	@Autowired
	CartDAO cartDAO;

	@GetMapping
	public String viewCart(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()
				&& !"anonymousUser".equals(authentication.getPrincipal())) {
			String userEmail = authentication.getName();
			User user = userService.findByEmail(userEmail);
			if (user != null) {
				List<Cart> cartItems = cartDAO.findByUser(user);
				model.addAttribute("cartList", cartItems);
				model.addAttribute("totalPrice", totalPrice(cartItems));
			}
		}
		return "cart";
	}

	public double totalPrice(List<Cart> cartItems) {
		double total = 0;
		for (Cart cartItem : cartItems) {
			total += cartItem.getProduct().getPrice() * cartItem.getQuantity();
		}
		return total;
	}
}