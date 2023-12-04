package com.greenfarm.dto;

import com.greenfarm.entity.Product;
import com.greenfarm.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Integer cartID;
    private Float quantity;
     
    Product product;
    User user;
}
