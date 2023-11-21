package com.greenfarm.dto;

import java.util.Date;
import java.util.List;

import com.greenfarm.entity.VoucherOrder;
import com.greenfarm.entity.VoucherUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoucherDTO {
	private Integer voucherid;
	private String code;
	private Float discount;
	private Date expirationdate = new Date();

	private List<VoucherUser> voucheruser;
	private List<VoucherOrder> voucherorder;
}
