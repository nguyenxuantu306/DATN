package com.greenfarm.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.greenfarm.entity.User;
import com.greenfarm.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLoginOAuth2successHandler implements AuthenticationSuccessHandler {
	
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	UserDetailsService details;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		 if (authentication != null) {
		        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		        String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
		        // provider là tên đăng ký của nhà cung cấp OAuth2 (ví dụ: "google", "facebook")
		      //  return "Logged in via " + provider ;
		        String email = oAuth2User.getAttribute("email");
		 	   try {
		 		  User dangnhap = userService.findByEmail(email);
		 		  if (dangnhap != null) {
		 			// Lấy thông tin đăng nhập từ người dùng
		    			//String username = dangnhap.getEmail();  // Giả sử email là tên người dùng

		    			setAuthenticationInSecurityContext(dangnhap.getEmail());
		    			// Tải thông tin người dùng từ UserDetailsService
//		    			UserDetails userDetails = details.loadUserByUsername(username);
//
//		    			// Tạo một đối tượng Authentication
//		    			Authentication authentication2 = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//
//		    			// Đặt thông tin xác thực vào SecurityContext
//		    			SecurityContext sc = SecurityContextHolder.getContext();
//		    			sc.setAuthentication(authentication2);
//		    			SecurityContext check = SecurityContextHolder.getContext();
		    			
		    			response.sendRedirect("/");
				}else {
					response.sendRedirect("/register/completelogin");
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				response.sendRedirect("/logoff");
			}
	}else {
		response.sendRedirect("/login");
	}
		 
		 
	

}
	
	private void setAuthenticationInSecurityContext(String username) {
	    // Tải thông tin người dùng từ UserDetailsService
	    UserDetails userDetails = details.loadUserByUsername(username);

	    // Tạo một đối tượng Authentication
	    Authentication authentication2 = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

	    // Đặt thông tin xác thực vào SecurityContext
	    SecurityContext sc = SecurityContextHolder.getContext();
	    sc.setAuthentication(authentication2);
	}
	}