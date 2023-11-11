package com.greenfarm.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Touroverviews")
public class TourOverview implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Positive(message = "Touroverviewid không được là số âm")
	private Integer touroverviewid;
	
	@NotBlank(message = "Title không được phép trống")
	private String Title;
	
	@NotBlank(message = "Content không được phép trống")
	private String Content;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "tourid")
	Tour tour;
	
}
