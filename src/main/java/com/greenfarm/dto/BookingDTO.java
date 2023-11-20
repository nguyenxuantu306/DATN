package com.greenfarm.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.PaymentMethod;
import com.greenfarm.entity.StatusBooking;
import com.greenfarm.entity.StatusOrder;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.User;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Bookingid; 
	
	private LocalDateTime bookingdate;
	
	private Float Totalprice;
	
	@NotNull(message = "Số lượng vé người lớn không được để trống")
    @Min(value = 1, message = "Số lượng vé người lớn phải lớn hơn hoặc bằng 1")
	private Integer Adultticketnumber;
	
	@NotNull(message = "Số lượng vé trẻ em không được để trống")
	@Min(value = 0, message = "Số lượng vé trẻ em không được nhỏ hơn 0")
	private Integer Childticketnumber;
	
	private User user;
	
	private Tour tour;
	
	private StatusBooking statusbooking;
	
	private PaymentMethod paymentmethod;
	
	
}