package com.greenfarm.Controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String addToCart(HttpSession session, @PathVariable("productId") Integer productId) {
        // Lấy thông tin người dùng hiện tại từ session hoặc Spring Security
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
                        // Nếu sản phẩm đã tồn tại trong giỏ hàng, tăng số lượng lên 1
                        cartItem.setQuantity(cartItem.getQuantity() + 1);
                    } else {
                        // Nếu sản phẩm chưa có trong giỏ hàng, tạo mới và đặt số lượng là 1
                        cartItem = new Cart();
                        cartItem.setUser(user);
                        cartItem.setProduct(product);
                        cartItem.setQuantity(1);
                    }
                    cartDAO.save(cartItem);
                }
                return "redirect:/product/shop"; // Điều hướng trở lại trang sản phẩm sau khi thêm vào giỏ hàng
            }
        }
        // Nếu người dùng chưa đăng nhập, điều hướng đến trang đăng nhập
        return "redirect:/login";
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
        double total = 0;
        for (Cart cartItem : cartItems.values()) {
            total += cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }
        return total;
    }
}
