package com.greenfarm.dto;

import java.util.List;

import com.greenfarm.entity.Booking;
import com.greenfarm.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDTO {
	private Integer Paymentmethodid;
	private String Methodname;
	private String Description;
	private List<Order> order;
	private List<Booking> booking;
}
