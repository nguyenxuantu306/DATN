package com.greenfarm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "JwtResponse")
public class JwtResponse {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ID;

    private String Token;
    private String Type = "Bearer";
    private String RefreshToken;
    private String UserID;
    private String Email;
    private String Role;
    
}
