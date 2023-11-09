package com.greenfarm.vnpay2;

import jakarta.servlet.http.HttpServletRequest;

public interface VNPayService {
    String createOrder(int totalPrice, String urlReturn);
    int orderReturn(HttpServletRequest request);
}
