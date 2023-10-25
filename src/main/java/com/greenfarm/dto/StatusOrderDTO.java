package com.greenfarm.dto;

import java.util.List;

import com.greenfarm.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusOrderDTO {
	private Integer statusorderid;
	
	private String name;
	private List<OrderDTO> order;
}
