package com.greenfarm.dto;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private Integer orderDetailId;
    private Integer quantityOrdered;
    private Float totalPrice;
    Order order;
	Product product; 
}