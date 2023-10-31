package com.greenfarm.service.impl;


import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;

import com.greenfarm.entity.CustomOAuth2User;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService{
	
	
	public OAuth2User loadUser(OAuth2UserRequest auth2UserRequest) {
		OAuth2User user =  super.loadUser(auth2UserRequest);
        return new CustomOAuth2User(user);
		
	}
}
