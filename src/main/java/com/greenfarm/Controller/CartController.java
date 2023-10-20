package com.greenfarm.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.greenfarm.dao.CartDAO;
import com.greenfarm.entity.Cart;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.User;
import com.greenfarm.service.ProductService;
import com.greenfarm.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
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

    @RequestMapping("/add/{productId}")
    public String viewAdd(ModelMap modelMap, HttpSession session, HttpServletRequest request,
            @PathVariable("productId") Integer productId) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            User user = userService.findById(userId);
            Product product = productService.findById(productId);

            if (user != null && product != null) {
                Cart cartItem = cartDAO.findByUserAndProduct(user, product);

                if (cartItem != null) {
                    // Update the existing cart item
                    cartItem.setQuantity(cartItem.getQuantity() + 1);
                } else {
                    // Create a new cart item
                    cartItem = new Cart();
                    cartItem.setUser(user);
                    cartItem.setProduct(product);
                    cartItem.setQuantity(1);
                }

                cartDAO.save(cartItem);
            }
        } else {
            return "redirect:/login";
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

	@RequestMapping("/update/{productId}")
	public String viewUpdate(ModelMap modelMap, HttpSession session, HttpServletRequest request,
			@PathVariable("productId") Integer productId) {
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
