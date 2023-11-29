package com.greenfarm.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;

import com.greenfarm.dto.Provider;
import com.greenfarm.entity.CustomOAuth2User;
import com.greenfarm.entity.User;
import com.greenfarm.service.UserService;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService{
	@Autowired
	UserService userService;
	
	 
    public CustomOAuth2UserService() {
        System.out.println("CustomOAuth2UserService bean is created.");
    }
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User user =  super.loadUser(userRequest);
		System.out.println("CustomOAuth2UserService invoked");
		//return new CustomOAuth2User(user);
		//CustomOAuth2User customOAuth2User = new CustomOAuth2User(user);
		return new CustomOAuth2User(super.loadUser(userRequest));
		
	}
}
