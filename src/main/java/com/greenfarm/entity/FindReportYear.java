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

@Entity
@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class FindReportYear implements Serializable {	
	@Id
//	private Integer yearValue;
	private Integer monthValue;
	Double sum;
	
	
	public FindReportYear(Integer monthValue, Double sum) {
		this.monthValue = monthValue;
		this.sum = sum;
	}
	
	
}
