package com.greenfarm.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RefreshToken")
public class RefreshToken {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;

    @OneToOne
    @JoinColumn(name = "UserId")
    private User user;

    @Column(unique = true)
    private String token;

    private Instant expiryDate;
}
