package com.greenfarm.dto;

import com.greenfarm.entity.Order;
import com.greenfarm.entity.PaymentMethod;
import com.greenfarm.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
	private Integer orderDetailid;
	private Integer quantityordered;
	private Float totalprice;
	private Order order;
	private Product product;
	private PaymentMethod paymentmethod;
}