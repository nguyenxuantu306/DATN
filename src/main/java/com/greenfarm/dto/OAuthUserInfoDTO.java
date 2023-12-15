package com.greenfarm.dto;

import org.springframework.security.core.Authentication;

import com.greenfarm.entity.CustomOAuth2User;

import java.util.List;
import java.util.stream.Collectors;

public class OAuthUserInfoDTO {
    private String sub;
    private String email;
    private boolean emailVerified;
    private String givenName;
    private String locale;
    private String picture;
    private List<String> authorities;

    // Constructors, Getters, and Setters
    

    public OAuthUserInfoDTO(String sub, String email, boolean emailVerified, String givenName, String locale, String picture, List<String> authorities) {
        this.sub = sub;
        this.email = email;
        this.emailVerified = emailVerified;
        this.givenName = givenName;
        this.locale = locale;
        this.picture = picture;
        this.authorities = authorities;
    }

    public OAuthUserInfoDTO() {
		super();
	}

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public List<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

	public static OAuthUserInfoDTO extractUserInfo(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomOAuth2User) {
            CustomOAuth2User customOAuth2User = (CustomOAuth2User) principal;

            OAuthUserInfoDTO userInfo = new OAuthUserInfoDTO(
                    customOAuth2User.getAttribute("sub"),
                    customOAuth2User.getAttribute("email"),
                    Boolean.parseBoolean(customOAuth2User.getAttribute("email_verified")),
                    customOAuth2User.getAttribute("given_name"),
                    customOAuth2User.getAttribute("locale"),
                    customOAuth2User.getAttribute("picture"),
                    customOAuth2User.getAuthorities()
                            .stream()
                            .map(Object::toString)
                            .collect(Collectors.toList())
            );

            return userInfo;
        }

        return null;
    }
}
