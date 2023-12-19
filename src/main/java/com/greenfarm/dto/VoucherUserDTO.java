package com.greenfarm.dto;

import java.time.LocalDateTime;

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
	private LocalDateTime expirationdate;
	private User user;
	private Voucher voucher;
}
