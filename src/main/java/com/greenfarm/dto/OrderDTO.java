package com.greenfarm.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.PaymentMethod;
import com.greenfarm.entity.StatusOrder;
import com.greenfarm.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

	private Integer Orderid;
	private LocalDateTime orderdate;

	private String address;

	List<OrderDetail> orderDetail;
	User user;
	StatusOrder statusOrder;
	private PaymentMethod paymentmethod;
	//private String formattedOrderDate;
}