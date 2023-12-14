package com.greenfarm.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greenfarm.dao.CartDAO;
import com.greenfarm.dao.OrderDAO;
import com.greenfarm.dao.OrderDetailDAO;
import com.greenfarm.dao.PaymentMethodDAO;
import com.greenfarm.dao.VoucherOrderDAO;
import com.greenfarm.dto.OrderDTO;
import com.greenfarm.entity.Cart;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.PaymentMethod;
import com.greenfarm.entity.StatusOrder;
import com.greenfarm.entity.User;
import com.greenfarm.entity.Voucher;
import com.greenfarm.entity.VoucherOrder;
import com.greenfarm.service.CartService;
import com.greenfarm.service.UserService;
import com.greenfarm.service.VNPayService;
import com.greenfarm.service.VoucherService;
import com.greenfarm.service.VoucherUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@CrossOrigin(origins = "http://localhost:8080")
public class VNPayController {

	@Autowired
	private UserService userService;

	@Autowired
	CartService cartService;

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

	@Autowired
	VoucherService voucherService;

	@Autowired
	VoucherOrderDAO voucherOrderDAO;

	@PostMapping("/submitOrder")
	public String submidOrder(@RequestParam("hiddenTotalPrice") int totalPrice,

			HttpServletRequest request, @RequestParam(name = "voucherid", required = false) String[] voucherIds) {
		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		String vnpayUrl = vnPayService.createOrder(totalPrice, baseUrl);

		HttpSession session = request.getSession();
		session.setAttribute("voucherid", voucherIds);

		return "redirect:" + vnpayUrl;
	}

	@GetMapping("/cancelVNpay")
	public String GetMapping(Model model, @ModelAttribute("Order") OrderDTO orderDTO, HttpServletRequest request) {
		int paymentStatus = vnPayService.orderReturn(request);

		if (paymentStatus != 1) {
			return "cancel";
		}

		// String orderInfo = request.getParameter("vnp_OrderInfo");
		// String paymentTime = request.getParameter("vnp_PayDate");
		// String transactionId = request.getParameter("vnp_TransactionNo");
		// String totalPrice = request.getParameter("vnp_Amount");

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
				// orderItem.setAddress(user.getAddress());
				orderItem.setStatusOrder(statusOrder);
				orderItem.setPaymentmethod(paymentMethodObj);
				orderDAO.save(orderItem);

				List<Cart> cartItems = cartDAO.findByUser(user);
				List<OrderDetail> orderDetailList = new ArrayList<>();

				float total = 0;
				for (Cart cartItem : cartItems) {
					OrderDetail orderDetailItem = new OrderDetail();
					orderDetailItem.setOrder(orderItem);
					orderDetailItem.setProduct(cartItem.getProduct());
					orderDetailItem.setQuantityordered(cartItem.getQuantity());
					orderDetailItem.setTotalPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice());
					orderDetailList.add(orderDetailItem);
					total += cartItem.getQuantity() * cartItem.getProduct().getPrice();

				}
				orderDetailDAO.saveAll(orderDetailList);

				float discountedTotal = 0;
				List<VoucherOrder> voucherLists = new ArrayList<>();
				HttpSession session = request.getSession();
				String[] voucherIds = (String[]) session.getAttribute("voucherid");

				if (voucherIds != null && voucherIds.length > 0 && !Arrays.asList(voucherIds).contains("0")) {
					for (String voucherId : voucherIds) {
						// Lấy thông tin Voucher từ voucherId
						Voucher voucher = voucherService.findByVoucherid(Long.parseLong(voucherId));

						// Tạo mới VoucherOrder và thêm vào danh sách
						VoucherOrder voucherOrder = new VoucherOrder();
						voucherOrder.setOrder(orderItem);
						voucherOrder.setVoucher(voucher);
						voucherLists.add(voucherOrder);

						// Áp dụng giảm giá từ voucher vào tổng giá trị đơn hàng
						discountedTotal = total - (total * voucher.getDiscount());
					}
				} else {
					System.out.println("heheehdeoco");
				}

				voucherOrderDAO.saveAll(voucherLists);

				model.addAttribute("totalDiscount", discountedTotal);
				model.addAttribute("orderConfirmation", orderItem);
				model.addAttribute("total", total);
				model.addAttribute("cartConfirmation", cartItems);
				cartService.delete(cartItems);
			}

			return "success";
		} else {
			System.out.println("Xin chào! Bạn chưa đăng nhập.");
			return "login";
		}
	}

}
