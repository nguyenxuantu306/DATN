package com.greenfarm.entity;

import java.io.Serializable;

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
@NoArgsConstructor
@AllArgsConstructor
public class Report implements Serializable {
	private static final long serialVersionUID = -5885342208000278840L;
	@Id
	Serializable group;
	Double sum;
	Long count;

	public Report(String group, Long count) {
		this.group  = group;
		this.count = count;
	}
}
