package com.greenfarm.service;

import jakarta.servlet.http.HttpServletRequest;

public interface VNPayService {
    String createOrder(int totalPrice, String urlReturn);
    int orderReturn(HttpServletRequest request);
}
