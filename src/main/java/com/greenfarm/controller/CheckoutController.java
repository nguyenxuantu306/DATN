package com.greenfarm.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.greenfarm.dao.CartDAO;
import com.greenfarm.dao.OrderDAO;
import com.greenfarm.dao.OrderDetailDAO;
import com.greenfarm.dao.PaymentMethodDAO;
import com.greenfarm.dto.OrderDTO;
import com.greenfarm.entity.Cart;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.PaymentMethod;
import com.greenfarm.entity.StatusOrder;
import com.greenfarm.entity.User;
import com.greenfarm.service.UserService;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private UserService userService;

    @Autowired
    CartDAO cartDAO;

    @Autowired
    OrderDAO orderDAO;

    @Autowired
    OrderDetailDAO orderDetailDAO;

    @Autowired
    PaymentMethodDAO paymentMethodDAO;

    ObjectMapper mapper = JsonMapper.builder() // or different mapper for other format
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            // and possibly other configuration, modules, then:
            .build();

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping
    public String checkout(ModelMap modelMap) {
        // Lấy thông tin người dùng đã xác thực từ SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Kiểm tra nếu người dùng đã xác thực
        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.findByEmail(userDetails.getUsername());
            modelMap.addAttribute("user", user);
            if (user != null) {
                List<Cart> cartItems = cartDAO.findByUser(user);
                modelMap.addAttribute("cartList", cartItems);
                modelMap.addAttribute("totalPrice", totalPrice(cartItems));
            }

            return "checkout";
        } else {
            System.out.println("Xin chào! Bạn chưa đăng nhập.");
            return "checkout";
        }
    }

    @PostMapping(value = "/payment")
    public String Payment(ModelMap modelMap, @ModelAttribute("Order") OrderDTO orderDTO,
            @RequestParam("paymentMethod") Integer paymentMethod) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.findByEmail(userDetails.getUsername());

            // Chuyển đổi thời gian hiện tại thành Date
//            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//            Date date = new Date();
//            System.out.println(df.format(date));

            LocalDateTime now = LocalDateTime.now();
            StatusOrder statusOrder = new StatusOrder();
            statusOrder.setStatusorderid(1);
            if (user != null) {
                Order orderItem = new Order();
                orderItem.setUser(user);
                orderItem.setOrderdate(now);
                orderItem.setAddress(orderDTO.getAddress());
                orderItem.setStatusOrder(statusOrder);
                System.out.println(orderDTO.getAddress());
                orderDAO.save(orderItem);

                List<Cart> cartItems = cartDAO.findByUser(user); // Retrieve cart items for the user

                List<OrderDetail> orderDetailList = new ArrayList<>();

                PaymentMethod paymentMethodObj = paymentMethodDAO.findById(paymentMethod).get();

                for (Cart cartItem : cartItems) {
                    OrderDetail orderDetailItem = new OrderDetail();
                    orderDetailItem.setOrder(orderItem);
                    orderDetailItem.setProduct(cartItem.getProduct());
                    orderDetailItem.setQuantityordered(cartItem.getQuantity());
                    orderDetailItem.setTotalPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice());
                    orderDetailItem.setPaymentMethod(paymentMethodObj);
                    orderDetailList.add(orderDetailItem);

                }

                orderDetailDAO.saveAll(orderDetailList);
            }

            return "success";
        } else {
            System.out.println("Xin chào! Bạn chưa đăng nhập.");
            return "checkout";
        }
    }

    public double totalPrice(List<Cart> cartItems) {
        double total = 0;
        for (Cart cartItem : cartItems) {
            total += cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }
        return total;
    }

}
