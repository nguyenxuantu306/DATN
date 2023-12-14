package com.greenfarm.entity;

import java.io.Serializable;

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
