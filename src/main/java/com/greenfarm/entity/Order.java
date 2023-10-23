package com.greenfarm.entity;

import java.io.Serializable;

import java.util.Date;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Orders")
public class Order implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Orderid;

	@ManyToOne
	@JoinColumn(name = "UserID")
	private User user;

	@Temporal(TemporalType.DATE)
	@JoinColumn(name = "OrderDate")
	private Date orderdate = new Date();

	private String Address;

	@JsonIgnore
	@OneToMany(mappedBy = "order")
	List<OrderDetail> orderDetail;
	
//	@ManyToOne
//	@JoinColumn(name = "statusorderid")
//	StatusOrder statusOrder;
}
