package com.greenfarm.dto;

import com.greenfarm.entity.Order;
import com.greenfarm.entity.Voucher;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
