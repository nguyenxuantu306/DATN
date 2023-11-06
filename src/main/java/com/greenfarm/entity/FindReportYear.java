package com.greenfarm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class FindReportYear implements Serializable {	
//	@Id
//	private Integer yearValue;
	private Integer month;
	Double sum;
	
	
	public FindReportYear(Integer month, Double sum) {
		this.month = month;
		this.sum = sum;
	}


	public Integer getMonth() {
		return month;
	}


	public void setMonth(Integer month) {
		this.month = month;
	}


	public Double getSum() {
		return sum;
	}


	public void setSum(Double sum) {
		this.sum = sum;
	}


	public FindReportYear() {
		super();
	}
	
}
