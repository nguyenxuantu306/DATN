package com.greenfarm.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
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
import java.time.format.DateTimeFormatter;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Orders")
public class Order implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer Orderid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "orderdate")
	private LocalDateTime orderdate;

	public String getOrderDateFormatted() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm a");
		return orderdate.format(formatter);
	}

	private String Address;

	// @JsonIgnore
	@OneToMany(mappedBy = "order")
	public List<OrderDetail> orderDetail;

	@ManyToOne
	@JoinColumn(name = "userid")
	User user;

	@ManyToOne
	@JoinColumn(name = "statusorderid")
	StatusOrder statusOrder;
}

//
//<div class="">
//Tổng hóa đơn: <span th:with="total=0"> <span
//	th:each="orderDetail : ${order.orderDetail}"> <span
//		th:with="subtotal=${orderDetail.quantityordered * orderDetail.product.price}">
//			<span th:with="total=${total + subtotal}"></span>
//	</span>
//</span> <span
//	th:text="'đ' + ${#numbers.formatDecimal(total, 0, 'COMMA', 0, 'POINT')}"></span>
//</span>
//</div>