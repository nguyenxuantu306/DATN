package com.greenfarm.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class TopSellingProductDTO {

    private Integer productid;
    private String productname;
    private BigDecimal price;
    private Integer totalquantityordered;
    private BigDecimal totalrevenue;
    private List<OrderDetail> orderDetails;
    private Product product;

    
}

