package com.greenfarm.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Vouchers")
public class Voucher implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer voucherid;
	
	private String code;
	private Float discount;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expirationdate")
	private LocalDateTime expirationdate;
	
	private Boolean isdeleted = Boolean.FALSE;
	
	@JsonIgnore
	@OneToMany(mappedBy = "voucher")
	List<VoucherUser> voucheruser;
	
	@JsonIgnore
	@OneToMany(mappedBy = "voucher")
	List<VoucherOrder> voucherorder;
	
	public void setIsDeleted(boolean isdeleted) {
	    this.isdeleted = isdeleted;
	}
}
