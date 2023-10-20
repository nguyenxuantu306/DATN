package com.greenfarm.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Tourpypetickets")
public class TourTypeTicket implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer TourTypeTicketID;
	
	private Double Price;
	
	@ManyToOne
	@JoinColumn(name = "tourid")
	Tour tour;
	
	@ManyToOne
	@JoinColumn(name = "Tickettypeid")
	TicketType ticketType;
	
	@JsonIgnore
	@OneToMany(mappedBy = "tourTypeTicket")
	List<Booking> booking;
}
