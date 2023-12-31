package com.greenfarm.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.greenfarm.entity.User;
import com.greenfarm.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CustomLogoutHandler implements LogoutHandler {

	@Autowired
	UserService userService;

	// @Override
	// public void logout(HttpServletRequest request, HttpServletResponse response,
	// Authentication authentication) {
	// SecurityContextHolder.clearContext();
	//
	// try {
	// final UserEntity userEntity =
	// userService.findUserByUsername(authentication.getName());
	// System.out.println("Logged Out Handler");
	// //set status to false
	// userEntity.setStatus(false);
	// userService.saveUser(userEntity);
	// //redirecting to another controller endpoint
	// response.sendRedirect("/perform-logout");
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		SecurityContextHolder.clearContext();
		try {
			final User userEntity = userService.findByEmail(authentication.getName());
			System.out.println("Logged Out Handler");
			// set status to false
			// redirecting to another controller endpoint
			response.sendRedirect("/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
