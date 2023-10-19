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
        }

        // Lấy URL trang hiện tại
        String referer = request.getHeader("Referer");

        // Chuyển hướng trở lại trang hiện tại sau khi thêm vào giỏ hàng
        return "redirect:" + referer;
    }

    @RequestMapping("update/{productId}")
    public String viewUpdate(ModelMap modelMap, HttpSession session, @PathVariable("productID") Integer productID) {
        HashMap<Integer, Cart> cartItems = (HashMap<Integer, Cart>) session.getAttribute("myCartItems");
        if (cartItems == null) {
            cartItems = new HashMap<>();
        }

        if (cartItems.containsKey(productID)) {
            Cart item = cartItems.get(productID);

            Integer updateQuantity = item.getQuantity() - 1;

            if (updateQuantity > 0) {
                item.setQuantity(updateQuantity);
                cartItems.put(productID, item);
            } else {
                cartItems.remove(productID);
            }
        }
        session.setAttribute("myCartItems", cartItems);
        session.setAttribute("myCartTotal", totalPrice(cartItems));
        session.setAttribute("myCartNum", cartItems.size());

        return "cart";
    }

    @RequestMapping("remove/{productId}")
    public String viewRemove(ModelMap modelMap, HttpSession session, @PathVariable("productID") Integer productID) {
        HashMap<Integer, Cart> cartItems = (HashMap<Integer, Cart>) session.getAttribute("myCartItems");
        if (cartItems == null) {
            cartItems = new HashMap<>();
        }
        if (cartItems.containsKey(productID)) {
            cartItems.remove(productID);
        }
        session.setAttribute("myCartItems", cartItems);
        session.setAttribute("myCartTotal", totalPrice(cartItems));
        session.setAttribute("myCartNum", cartItems.size());
        return "cart";
    }

    public double totalPrice(HashMap<Integer, Cart> cartItems) {
        int count = 0;
        for (Map.Entry<Integer, Cart> list : cartItems.entrySet()) {
            count += list.getValue().getProduct().getPrice() * list.getValue().getQuantity();
        }
        return count;
    }
}
