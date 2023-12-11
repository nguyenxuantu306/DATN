package com.greenfarm.token;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@SuppressWarnings("serial")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TokenRefreshRequest")
public class TokenRefreshRequest {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ID;

    @NotBlank
    private String refreshToken;
}
