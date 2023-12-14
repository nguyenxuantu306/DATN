package com.greenfarm.dto;

import com.greenfarm.entity.User;
import com.greenfarm.entity.Voucher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherUserDTO {

	private Integer voucheruserid;
	private User user;
	private Voucher voucher;
}
