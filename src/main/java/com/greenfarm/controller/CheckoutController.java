package com.greenfarm.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
import com.greenfarm.entity.Voucher;
import com.greenfarm.entity.VoucherUser;
import com.greenfarm.service.UserService;
import com.greenfarm.service.VoucherUserService;
import com.greenfarm.vnpay2.VNPayService;

import jakarta.servlet.http.HttpServletRequest;

@Controller

public class CheckoutController {

	@Autowired
    private VNPayService vnPayService;
	
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

	@Autowired
	VoucherUserService voucherUserService;

	@GetMapping("/checkout")
	public String Checkout(ModelMap modelMap, HttpServletRequest request,Model model) {
		
		
		// Lấy thông tin người dùng đã xác thực từ SecurityContextHolder
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		// Kiểm tra nếu người dùng đã xác thực
		if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			User user = userService.findByEmail(userDetails.getUsername());
			
			List<VoucherUser> vouchers = voucherUserService.findByUser(user);
			
			if (vouchers != null && !vouchers.isEmpty()) {
			    model.addAttribute("vouchers", vouchers);
			} else {
			    model.addAttribute("error", "Không tìm thấy vouchers hoặc danh sách rỗng.");
			}
			
			
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

	@GetMapping("/checkoutPayment")
	public String CheckoutPayment(ModelMap modelMap) {
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

			return "checkoutPayment";
		} else {
			System.out.println("Xin chào! Bạn chưa đăng nhập.");
			return "login";
		}
	}

	@PostMapping("/checkout/payment")
	public String Payment(ModelMap modelMap, @ModelAttribute("Order") OrderDTO orderDTO,
			@RequestParam("paymentMethod") Integer paymentMethod) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			User user = userService.findByEmail(userDetails.getUsername());

			LocalDateTime now = LocalDateTime.now();
			StatusOrder statusOrder = new StatusOrder();
			statusOrder.setStatusorderid(1);
			PaymentMethod paymentMethodObj = paymentMethodDAO.findById(paymentMethod).get();
			if (user != null) {
				Order orderItem = new Order();
				orderItem.setUser(user);
				orderItem.setOrderdate(now);
				orderItem.setAddress(orderDTO.getAddress());
				orderItem.setStatusOrder(statusOrder);
				orderItem.setPaymentmethod(paymentMethodObj);
				System.out.println(orderDTO.getAddress());
				orderDAO.save(orderItem);

				List<Cart> cartItems = cartDAO.findByUser(user); // Retrieve cart items for the user
				List<OrderDetail> orderDetailList = new ArrayList<>();

				for (Cart cartItem : cartItems) {
					OrderDetail orderDetailItem = new OrderDetail();
					orderDetailItem.setOrder(orderItem);
					orderDetailItem.setProduct(cartItem.getProduct());
					orderDetailItem.setQuantityordered(cartItem.getQuantity());
					orderDetailItem.setTotalPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice());
					// orderDetailItem.setPaymentMethod(paymentMethodObj);
					orderDetailList.add(orderDetailItem);

				}

				orderDetailDAO.saveAll(orderDetailList);

			}

			return "redirect:/success";
		} else {
			System.out.println("Xin chào! Bạn chưa đăng nhập.");
			return "login";
		}
	}

	public long totalPrice(List<Cart> cartItems) {
		long total = 0;
		for (Cart cartItem : cartItems) {
			total += cartItem.getProduct().getPrice() * cartItem.getQuantity();
		}
		return total;
	}
	
	@PostMapping("/submitOrder")
    public String submidOrder(@RequestParam("totalPrice") int totalPrice,
                            
                              HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(totalPrice, baseUrl);

        return "redirect:" + vnpayUrl;
    }
	
	@GetMapping("/vnPayPayment")
    public String GetMapping(HttpServletRequest request,Model model) {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

//        Map<String, Object> res = new HashMap<>();
//        res.put("orderId", orderInfo);
//        res.put("totalPrice", totalPrice);
//        res.put("paymentTime", paymentTime);
//        res.put("transactionId", transactionId);
        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);
        
//        return new ResponseEntity<>(res, HttpStatus.OK);
        return "ordersuccess" ;
    }

}
