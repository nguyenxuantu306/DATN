package com.greenfarm.dto;

import java.util.Date;

import com.greenfarm.entity.Voucher;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
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
}
