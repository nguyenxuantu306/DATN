package com.greenfarm.controller;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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
import com.greenfarm.entity.AbstractEmailContext;
import com.greenfarm.entity.Address;
import com.greenfarm.entity.Cart;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.PaymentMethod;
import com.greenfarm.entity.StatusOrder;
import com.greenfarm.entity.User;
import com.greenfarm.entity.Voucher;
import com.greenfarm.entity.VoucherOrder;
import com.greenfarm.entity.VoucherUser;
import com.greenfarm.service.AddressService;
import com.greenfarm.service.CartService;
import com.greenfarm.service.EmailService;
import com.greenfarm.service.UserService;
import com.greenfarm.service.VoucherService;
import com.greenfarm.service.VoucherUserService;
import com.greenfarm.service.impl.AccountVerificationEmailContext;
import com.greenfarm.service.impl.OrderConfirmEmailContext;
import com.greenfarm.service.impl.OrderEmailContext;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@CrossOrigin(origins = "http://localhost:8080")
public class CheckoutController {

	@Autowired
	EmailService emailService;

	@Autowired
	private UserService userService;

	@Autowired
	CartDAO cartDAO;

	@Autowired
	CartService cartService;

	@Autowired
	OrderDAO orderDAO;

	@Autowired
	OrderDetailDAO orderDetailDAO;

	@Autowired
	PaymentMethodDAO paymentMethodDAO;

	@Autowired
	VoucherUserService voucherUserService;

	@Autowired
	VoucherService voucherService;

	@Autowired
	VoucherOrderDAO voucherOrderDAO;

	@Autowired
	AddressService addressService;

	@GetMapping("/checkout")
	public String Checkout(ModelMap modelMap, HttpServletRequest request, Model model) {

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

	@PostMapping("/checkout/payment")
	public String Payment(Model model, @ModelAttribute("Order") OrderDTO orderDTO,
			@RequestParam(name = "voucherid", required = false) String[] voucherIds,
			@RequestParam(name = "addressId") Integer addressId, HttpServletRequest request) throws MessagingException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			User user = userService.findByEmail(userDetails.getUsername());

			LocalDateTime now = LocalDateTime.now();
			StatusOrder statusOrder = new StatusOrder();
			statusOrder.setStatusorderid(1);
			PaymentMethod paymentMethodObj = paymentMethodDAO.findById(1).get();
			Address address = addressService.findByAddressid(addressId);

			if (user != null) {
				Order orderItem = new Order();
				orderItem.setUser(user);
				orderItem.setOrderdate(now);
				orderItem.setAddress(address);
				orderItem.setStatusOrder(statusOrder);
				orderItem.setPaymentmethod(paymentMethodObj);
				orderItem.setVoucherorder(orderDTO.getVoucherorder());
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
//				String[] voucherIds = request.getParameterValues("voucherid");

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
				}
				List<String> formattedDiscounts = new ArrayList<>();
				DecimalFormat decimalFormat = new DecimalFormat("#");

				for (VoucherOrder voucherOrder : voucherLists) {
					String formattedDiscount = decimalFormat.format(voucherOrder.getVoucher().getDiscount() * 100);
					formattedDiscounts.add(formattedDiscount);
				}

				// Add the formatted discounts to the model
				model.addAttribute("formattedDiscounts", formattedDiscounts);

				voucherOrderDAO.saveAll(voucherLists);
				if (discountedTotal == 0) {
					discountedTotal = total;
				}
			
				
				model.addAttribute("totalDiscount", discountedTotal);
				model.addAttribute("discount", voucherLists);
				model.addAttribute("total", total);
				model.addAttribute("orderConfirmation", orderItem);
				model.addAttribute("cartConfirmation", cartItems);
				cartService.delete(cartItems);
				
				// gửi mail khi thành công
				OrderConfirmEmailContext confirmEmailContext = new OrderConfirmEmailContext();
				confirmEmailContext.init(orderItem, orderDetailList,total,discountedTotal);
				emailService.sendMail(confirmEmailContext);

			}

			return "success";

		} else

		{
			System.out.println("Xin chào! Bạn chưa đăng nhập.");
			return "login";
		}
	}

	private long totalPrice(List<Cart> cartItems) {
		long total = 0;
		for (Cart cartItem : cartItems) {
			total += cartItem.getProduct().getPrice() * cartItem.getQuantity();
		}
		return total;
	}

}
