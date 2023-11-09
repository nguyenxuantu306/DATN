package com.greenfarm.vnpay2;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

@Controller
public class VNPayController {

    @Autowired
    private VNPayService vnPayService;

	/*
	 * @GetMapping("/index2") public String home1(){ return "checkout"; }
	 */

//    @PostMapping("/submitOrder")
//    public String submidOrder(@RequestParam("totalPrice") int totalPrice,
//                            
//                              HttpServletRequest request){
//        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
//        String vnpayUrl = vnPayService.createOrder(totalPrice, baseUrl);
//
//        return "redirect:" + vnpayUrl;
//    }


//    @GetMapping("/vnPayPayment")
//    public String GetMapping(HttpServletRequest request,Model model) {
//        int paymentStatus = vnPayService.orderReturn(request);
//
//        String orderInfo = request.getParameter("vnp_OrderInfo");
//        String paymentTime = request.getParameter("vnp_PayDate");
//        String transactionId = request.getParameter("vnp_TransactionNo");
//        String totalPrice = request.getParameter("vnp_Amount");
//
////        Map<String, Object> res = new HashMap<>();
////        res.put("orderId", orderInfo);
////        res.put("totalPrice", totalPrice);
////        res.put("paymentTime", paymentTime);
////        res.put("transactionId", transactionId);
//        model.addAttribute("orderId", orderInfo);
//        model.addAttribute("totalPrice", totalPrice);
//        model.addAttribute("paymentTime", paymentTime);
//        model.addAttribute("transactionId", transactionId);
//        
////        return new ResponseEntity<>(res, HttpStatus.OK);
//        return "ordersuccess" ;
//    }
}
