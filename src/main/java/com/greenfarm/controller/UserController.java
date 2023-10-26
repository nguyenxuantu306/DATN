package com.greenfarm.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
	@RequestMapping("/api/user-email")
	@ResponseBody
	public String getCurrentUserEmail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		System.out.println(email);
		return email;

		// if (authentication != null && authentication.getPrincipal() instanceof
		// UserDetails) {
		// UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		// return ((CustomUserDetails) userDetails).getEmail();
		// } else {
		// return "Người dùng chưa đăng nhập";
		// }
	}

}
