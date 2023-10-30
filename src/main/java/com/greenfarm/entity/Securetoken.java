package com.greenfarm.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "securetokens")
public class Securetoken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String token;

	@CreationTimestamp
	@Column(updatable = false)
	private java.sql.Timestamp timeStamp;

	@Column(updatable = false)
	@Basic(optional = false)
	private LocalDateTime expireAt;

	@ManyToOne
	@JoinColumn(name = "userid")
	private User user;

	@Transient
	private boolean isExpired;

	public boolean isExpired() {
		return getExpireAt().isBefore(LocalDateTime.now()); // this is generic implementation, you can always make it
															// timezone specific
	}
}
