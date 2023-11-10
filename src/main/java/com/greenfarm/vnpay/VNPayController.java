package com.greenfarm.vnpay;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
import com.greenfarm.service.VoucherUserService;

import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "http://localhost:8080")
public class VNPayController {

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
	
	@Autowired
	private VNPayService vnPayService;

	

	@PostMapping("/submitOrder")
	public String submidOrder(@RequestParam("totalPrice") int totalPrice,

			HttpServletRequest request) {
		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		String vnpayUrl = vnPayService.createOrder(totalPrice, baseUrl);

		return "redirect:" + vnpayUrl;
	}

	@GetMapping("/vnPayPayment")
	public String GetMapping(HttpServletRequest request, Model model, 
			@ModelAttribute("Order") OrderDTO orderDTO) {
		int paymentStatus = vnPayService.orderReturn(request);

		String orderInfo = request.getParameter("vnp_OrderInfo");
		String paymentTime = request.getParameter("vnp_PayDate");
		String transactionId = request.getParameter("vnp_TransactionNo");
		String totalPrice = request.getParameter("vnp_Amount");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			User user = userService.findByEmail(userDetails.getUsername());

			LocalDateTime now = LocalDateTime.now();
			StatusOrder statusOrder = new StatusOrder();
			statusOrder.setStatusorderid(1);
			PaymentMethod paymentMethodObj = paymentMethodDAO.findById(2).get();
			if (user != null) {
				Order orderItem = new Order();
				orderItem.setUser(user);
				orderItem.setOrderdate(now);
				orderItem.setAddress(user.getAddress());
				orderItem.setStatusOrder(statusOrder);
				orderItem.setPaymentmethod(paymentMethodObj);
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
				model.addAttribute("orderConfirmation", orderItem);
			}

			return "/success";
		} else {
			System.out.println("Xin chào! Bạn chưa đăng nhập.");
			return "login";
		}
	}
}
