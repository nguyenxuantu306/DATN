package com.greenfarm.dto;

import com.greenfarm.entity.Order;
import com.greenfarm.entity.Voucher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherOrderDTO {
	private Integer voucherorderid;

	private Order order;

	private Voucher voucher;

}
