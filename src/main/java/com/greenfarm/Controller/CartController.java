package com.greenfarm.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.greenfarm.entity.Cart;
import com.greenfarm.entity.Product;
import com.greenfarm.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {
	@Autowired
	private ProductService productService;

	@RequestMapping("/add/{productId}")
	public String viewAdd(ModelMap modelMap, HttpSession session, HttpServletRequest request,
			@PathVariable("productId") Integer productId) {
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId != null) {
			HashMap<Integer, Cart> cartItems = (HashMap<Integer, Cart>) session.getAttribute("myCartItems");
			if (cartItems == null) {
				cartItems = new HashMap<>();
			}

			Product product = productService.findById(productId);
			if (product != null) {
				if (cartItems.containsKey(productId)) {
					Cart item = cartItems.get(productId);
					item.setProduct(product);
					item.setQuantity(item.getQuantity() + 1);
					cartItems.put(productId, item);
				} else {
					Cart item = new Cart();
					item.setProduct(product);
					item.setQuantity(1);
					cartItems.put(productId, item);
				}
			}

			session.setAttribute("myCartItems", cartItems);
			session.setAttribute("myCartTotal", totalPrice(cartItems));
			session.setAttribute("myCartNum", cartItems.size());
		} else {
			
			return "redirect:/login";
		}

		String referer = request.getHeader("Referer");
		return "redirect:" + referer;
	}

	@RequestMapping("/update/{productId}")
	public String viewUpdate(ModelMap modelMap, HttpSession session, HttpServletRequest request, @PathVariable("productId") Integer productId) {
		HashMap<Integer, Cart> cartItems = (HashMap<Integer, Cart>) session.getAttribute("myCartItems");
		if (cartItems != null) {
			if (cartItems.containsKey(productId)) {
				Cart item = cartItems.get(productId);

				Integer updateQuantity = item.getQuantity() - 1;

				if (updateQuantity > 0) {
					item.setQuantity(updateQuantity);
					cartItems.put(productId, item);
				} else {
					cartItems.remove(productId);
				}

				session.setAttribute("myCartItems", cartItems);
				session.setAttribute("myCartTotal", totalPrice(cartItems));
				session.setAttribute("myCartNum", cartItems.size());
			}
		}
		String referer = request.getHeader("Referer");

		return "redirect:" + referer;
	}

	@RequestMapping("/remove/{productId}")
	public String viewRemove(ModelMap modelMap, HttpSession session, @PathVariable("productId") Integer productId) {
		HashMap<Integer, Cart> cartItems = (HashMap<Integer, Cart>) session.getAttribute("myCartItems");
		if (cartItems != null) {
			cartItems.remove(productId);
			session.setAttribute("myCartItems", cartItems);
			session.setAttribute("myCartTotal", totalPrice(cartItems));
			session.setAttribute("myCartNum", cartItems.size());
		}
		return "cart"; 
	}

	public double totalPrice(HashMap<Integer, Cart> cartItems) {
		int count = 0;
		for (Map.Entry<Integer, Cart> entry : cartItems.entrySet()) {
			Cart item = entry.getValue();
			count += item.getProduct().getPrice() * item.getQuantity();
		}
		return count;
	}
}
