package com.greenfarm.Controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.greenfarm.dao.CartDAO;
import com.greenfarm.entity.Cart;
import com.greenfarm.entity.User;
import com.greenfarm.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private CartDAO cartDAO;

    @Autowired
    private UserService userService;

    public String home(Model model, HttpSession session) {
        // Kiểm tra trạng thái đăng nhập bằng Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            String userEmail = authentication.getName();
            User user = userService.findByEmail(userEmail);
            if (user != null) {
                // Lấy danh sách sản phẩm trong giỏ hàng từ session hoặc database
                HashMap<Integer, Cart> cartItems = (HashMap<Integer, Cart>) session.getAttribute("myCartItems");

                // Thực hiện kiểm tra và lấy dữ liệu từ database nếu cần
                if (cartItems == null) {
                    cartItems = cartDAO.getCartItemsByUser(user);
                }

                if (cartItems != null && !cartItems.isEmpty()) {
                    // Tính tổng giá trị
                    double total = 0;
                    for (Cart cartItem : cartItems.values()) {
                        total += cartItem.getProduct().getPrice() * cartItem.getQuantity();
                    }

                    model.addAttribute("cartItems", cartItems);
                    model.addAttribute("total", total);
                }
            }
        } else {
            // Nếu người dùng không đăng nhập, chuyển hướng đến trang đăng nhập
            return "redirect:/login";
        }

        return "index"; // Trang chính, bạn có thể đổi tên trang theo tên thật của trang của bạn
    }

    @RequestMapping("/profile")
    public String Profile(Model model) {
        return "profile";
    }

    @RequestMapping("/shop-list")
    public String ShopList(Model model) {
        return "product/shopList";
    }

    @RequestMapping("/tour")
    public String Tour(Model model) {
        return "tour/booking";
    }

    @RequestMapping("/tour-detail")
    public String TourDetail(Model model) {
        return "tour/detail";
    }

    @RequestMapping("/login")
    public String Login(Model model) {
        return "login";
    }

    @RequestMapping("/register")
    public String Register(Model model) {
        return "register";
    }

    @RequestMapping("/cart")
    public String Cart(Model model) {
        return "cart";
    }

    @RequestMapping({ "/admin", "/admin/home/index" })
    public String admin() {
        return "redirect:/assetsAdmin/admin/index.html";
    }

}
