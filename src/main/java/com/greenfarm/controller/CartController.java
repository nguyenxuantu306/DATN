package com.greenfarm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greenfarm.dao.CartDAO;
import com.greenfarm.entity.Cart;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.User;
import com.greenfarm.service.ProductService;
import com.greenfarm.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {
	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

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

	@RequestMapping("/add/{productId}")
	public String addToCart(HttpSession session, @PathVariable("productId") Integer productId, @RequestParam("quantity") Float quantity) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()
				&& !"anonymousUser".equals(authentication.getPrincipal())) {
			String userEmail = authentication.getName();
			User user = userService.findByEmail(userEmail);
			if (user != null) {
				Product product = productService.findById(productId);
				if (product != null) {
					Cart cartItem = cartDAO.findByUserAndProduct(user, product);
					if (cartItem != null) {
						cartItem.setQuantity(cartItem.getQuantity() + quantity);
					} else {
						cartItem = new Cart();
						cartItem.setUser(user);
						cartItem.setProduct(product);
						cartItem.setQuantity(quantity);
					}
					cartDAO.save(cartItem);
				}
				return "redirect:/product/shop";
			}
		}
		return "login";
	}

	@RequestMapping("/update/{productId}")
	public String viewUpdate(Model model,
			@PathVariable("productId") Integer productId,
			@RequestParam("quantity") Float newQuantity) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()
				&& !"anonymousUser".equals(authentication.getPrincipal())) {
			String userEmail = authentication.getName();
			User user = userService.findByEmail(userEmail);
			if (user != null) {
				Product product = productService.findById(productId);
				if (product != null) {
					Cart cartItem = cartDAO.findByUserAndProduct(user, product);
					if (cartItem != null) {
						if (newQuantity > 0) {
							cartItem.setQuantity(newQuantity);
							cartDAO.save(cartItem);
						} else {
							cartDAO.delete(cartItem);
						}
					}

					List<Cart> cartItems = cartDAO.findByUser(user);
					model.addAttribute("cartItems", cartItems);
					model.addAttribute("totalPrice", totalPrice(cartItems));
				}
			}
		}

		return "cart";
	}

	@RequestMapping("/remove/{productId}")
	public String viewRemove(Model model, @PathVariable("productId") Integer productId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()
				&& !"anonymousUser".equals(authentication.getPrincipal())) {
			String userEmail = authentication.getName();
			User user = userService.findByEmail(userEmail);
			if (user != null) {
				Product product = productService.findById(productId);
				if (product != null) {
					Cart cartItem = cartDAO.findByUserAndProduct(user, product);
					if (cartItem != null) {
						cartDAO.delete(cartItem); // Remove the item from the cart in the database
					}
					// Fetch the updated cart items from the database
					List<Cart> cartItems = cartDAO.findByUser(user);
					model.addAttribute("cartItems", cartItems);
					model.addAttribute("totalPrice", totalPrice(cartItems));
				}
			}
		}
		return "cart";
	}

	@RequestMapping("/remove/all")
	public String removeAllItems() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()
				&& !"anonymousUser".equals(authentication.getPrincipal())) {
			String userEmail = authentication.getName();
			User user = userService.findByEmail(userEmail);
			if (user != null) {
				// Lấy tất cả các mục giỏ hàng của người dùng và xóa chúng
				List<Cart> cartItems = cartDAO.findByUser(user);
				for (Cart cartItem : cartItems) {
					cartDAO.delete(cartItem); // Xóa từng mục giỏ hàng
				}
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
