package com.greenfarm.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Report7day implements Serializable {
	private static final long serialVersionUID = -5885342208000278840L;

	@Id
	private Date date;
	Double sum;

	// Constructor
	public Report7day(Date date, Double sum) {
		this.date = date;
		this.sum = sum;
	}
}
