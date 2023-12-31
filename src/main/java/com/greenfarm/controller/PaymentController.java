package com.greenfarm.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.greenfarm.entity.Address;
import com.greenfarm.entity.Cart;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.PaymentMethod;
import com.greenfarm.entity.StatusOrder;
import com.greenfarm.entity.User;
import com.greenfarm.entity.Voucher;
import com.greenfarm.entity.VoucherOrder;
import com.greenfarm.service.AddressService;
import com.greenfarm.service.CartService;
import com.greenfarm.service.EmailService;
import com.greenfarm.service.PaypalService;
import com.greenfarm.service.UserService;
import com.greenfarm.service.VoucherService;
import com.greenfarm.service.VoucherUserService;
import com.greenfarm.service.impl.OrderConfirmEmailContext;
import com.greenfarm.utils.PaypalPaymentIntent;
import com.greenfarm.utils.PaypalPaymentMethod;
import com.greenfarm.utils.Utils;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

@Controller
public class PaymentController {
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

	@Autowired
	EmailService emailService;
	
	public static final String URL_PAYPAL_SUCCESS = "success";
	public static final String URL_PAYPAL_CANCEL = "pay/cancel";

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private PaypalService paypalService;

	@PostMapping("/submitOrderPaypal")
	public String pay(HttpServletRequest request, @RequestParam("hiddenTotalPrice") double totalPrice,
			@RequestParam(name = "voucherid", required = false) String[] voucherIds,
			@RequestParam(name = "addressId") Integer addressId) {
		String cancelUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
		String successUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;

		try {
			Payment payment = paypalService.createPayment(totalPrice, "USD", PaypalPaymentMethod.paypal,
					PaypalPaymentIntent.sale, "payment description", cancelUrl, successUrl);
			HttpSession session = request.getSession();
			session.setAttribute("voucherid", voucherIds);
			session.setAttribute("addressId", addressId);
			for (Links links : payment.getLinks())
				if (links.getRel().equals("approval_url"))
					return "redirect:" + links.getHref();
		}

		catch (PayPalRESTException e) {
			log.error(e.getMessage());
		}

		return "/checkout";
	}

	@GetMapping("/pay/cancel")
	public String cancelPay() {
		return "cancel.html";
	}

	@GetMapping(URL_PAYPAL_SUCCESS)
	public String successPay(@RequestParam("paymentId") String paymentId, HttpServletRequest request,
			@RequestParam("PayerID") String payerId, @ModelAttribute("Order") OrderDTO orderDTO, Model model) throws MessagingException {
		try {
			Payment payment = paypalService.executePayment(paymentId, payerId);
			if (payment.getState().equals("approved")) {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
					UserDetails userDetails = (UserDetails) authentication.getPrincipal();
					User user = userService.findByEmail(userDetails.getUsername());

					LocalDateTime now = LocalDateTime.now();
					StatusOrder statusOrder = new StatusOrder();
					statusOrder.setStatusorderid(1);
					PaymentMethod paymentMethodObj = paymentMethodDAO.findById(3).get();

					HttpSession session = request.getSession();
					Integer addressId = (Integer) session.getAttribute("addressId");
					Address address = addressService.findByAddressid(addressId);

					if (user != null) {
						Order orderItem = new Order();
						orderItem.setUser(user);
						orderItem.setOrderdate(now);
						orderItem.setAddress(address);
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
							System.out.println("Không có mã giảm giá!");
						}
						List<String> formattedDiscounts = new ArrayList<>();
						DecimalFormat decimalFormat = new DecimalFormat("#");

						for (VoucherOrder voucherOrder : voucherLists) {
							String formattedDiscount = decimalFormat
									.format(voucherOrder.getVoucher().getDiscount() * 100);
							formattedDiscounts.add(formattedDiscount);
						}

						// Add the formatted discounts to the model
						model.addAttribute("formattedDiscounts", formattedDiscounts);

						voucherOrderDAO.saveAll(voucherLists);
						if (discountedTotal == 0) {
							discountedTotal = total;
						}
						model.addAttribute("discount", voucherLists);
						model.addAttribute("totalDiscount", discountedTotal);
						model.addAttribute("orderConfirmation", orderItem);
						model.addAttribute("total", total);
						model.addAttribute("cartConfirmation", cartItems);
						cartService.delete(cartItems);
						
						OrderConfirmEmailContext confirmEmailContext = new OrderConfirmEmailContext();
						confirmEmailContext.init(orderItem, orderDetailList,total,formattedDiscounts,voucherLists, discountedTotal);
						emailService.sendMail(confirmEmailContext);
					}
				} else {
					System.out.println("Xin chào! Bạn chưa đăng nhập.");
					return "login";
				}
			}
			return "success";
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());
		}

		return "/";
	}

}
