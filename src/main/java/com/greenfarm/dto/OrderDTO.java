package com.greenfarm.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.greenfarm.entity.Address;
import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.PaymentMethod;
import com.greenfarm.entity.StatusOrder;
import com.greenfarm.entity.User;
import com.greenfarm.entity.VoucherOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

	private Integer Orderid;
	private LocalDateTime orderdate;
	private Address address;
	private List<OrderDetail> orderDetail;
	User user;
	StatusOrder statusOrder;
	private PaymentMethod paymentmethod;
	private List<VoucherOrder> voucherorder;
}