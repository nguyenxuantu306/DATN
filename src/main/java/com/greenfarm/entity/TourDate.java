package com.greenfarm.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Tourdate")
public class TourDate implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Positive(message = "tourdateid không được là số âm")
	private Integer tourdateid;
	
	@Temporal(TemporalType.DATE)
	private Date tourdates;
	
	private Integer Availableslots;
	
	@ManyToOne
	@JoinColumn(name = "tourid")
	private Tour tour;
}
