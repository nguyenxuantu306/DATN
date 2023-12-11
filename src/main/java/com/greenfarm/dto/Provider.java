package com.greenfarm.dto;

import jakarta.persistence.Table;

@Table(name = "provider")
public enum Provider {
	LOCAL, GOOGLE, FACEBOOK, GITHUB
}
