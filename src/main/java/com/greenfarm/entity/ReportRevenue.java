package com.greenfarm.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportRevenue implements Serializable {
	private static final long serialVersionUID = -5885342208000278840L;
	@Id
	Serializable group;
	Long count;
	Integer tinh;

	public ReportRevenue(String group, Long count) {
		this.group = group;
		this.count = count;
	}

	public ReportRevenue(Integer tinh, Long count) {
		this.tinh = tinh;
		this.count = count;
	}

}
