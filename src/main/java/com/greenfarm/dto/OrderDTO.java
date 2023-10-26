package com.greenfarm.dto;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfarm.entity.OrderDetail;
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
	private Date orderdate;
	private String address;

	List<OrderDetail> orderDetail;
	User user;
	StatusOrder statusOrder;
}