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
public class CustomOAuth2successHandler implements AuthenticationSuccessHandler {
	
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	UserDetailsService details;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		OAuth2User login = (OAuth2User) authentication.getPrincipal();
		System.out.println("CustomOAuth2UserService invoked");
		OAuth2AuthenticationToken oauth2 = (OAuth2AuthenticationToken) authentication.getPrincipal();
		System.out.println("day la xem facebook co gi"+oauth2);
		// Lấy thông tin từ oauth2User
	    String email = login.getAttribute("email");
	    String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
	       
	    // Kiểm tra xem người dùng đã tồn tại trong cơ sở dữ liệu chưa
	 //   User user2 = userService.findByEmailAndProvider(email, Provider.GOOGLE); // Thay Provider.GOOGLE bằng giá trị tương ứng
	    try {
	    	 User dangnhap = userService.findByEmail(email);
	    	// Optional<User> account = Optional.ofNullable(userService.findByEmail(email));
		    	if (dangnhap != null) {
		    		
		    		try {
		    				
		    			// Lấy thông tin đăng nhập từ người dùng
		    			String username = dangnhap.getEmail();  // Giả sử email là tên người dùng

		    			// Tải thông tin người dùng từ UserDetailsService
		    			UserDetails userDetails = details.loadUserByUsername(username);

		    			// Tạo một đối tượng Authentication
		    			Authentication authentication2 = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

		    			// Đặt thông tin xác thực vào SecurityContext
		    			SecurityContext sc = SecurityContextHolder.getContext();
		    			sc.setAuthentication(authentication2);

			    			// Lấy thông tin đăng nhập từ người dùng
//			    			String username = dangnhap.getEmail();  // Giả sử email là tên người dùng
//			    			String password = dangnhap.getPassword();  // Giả sử password là mật khẩu
//
//			    			// Tạo một đối tượng Authentication
//			    			Authentication authentication2 = new UsernamePasswordAuthenticationToken(username, password);
//
//			    			// Đặt thông tin xác thực vào SecurityContext
//			    			SecurityContext sc = SecurityContextHolder.getContext();
//			    			sc.setAuthentication(authentication2);

			    		SecurityContext check = SecurityContextHolder.getContext();

			    		response.sendRedirect("/");
//						}else {
//							dangnhap.setProvider(Provider.GOOGLE);
//							dangnhap.setFirstname(login.getAttribute("given_name"));
//							userService.update(dangnhap);
//							//authentication = (Authentication) details.loadUserByUsername(dangnhap.getEmail());
//							//set lại  để biểu diễn người dùng đã đăng nhập mà tôi muốn
//			    			this.setlogin(dangnhap);
//			    			 SecurityContext check = SecurityContextHolder.getContext();
//							System.out.println(check);
//							response.sendRedirect("/");
//						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						authentication.setAuthenticated(false);

			            // Xóa thông tin đăng nhập
			            SecurityContextHolder.getContext().setAuthentication(null);
			       
						response.sendRedirect("/login");
					}
		    		
					
				}else {
					// Thêm thông tin cần chuyển đi vào RedirectAttributes
//		            RedirectAttributes redirectAttributes = (RedirectAttributes) authentication;
//		            redirectAttributes.addFlashAttribute("logininfo", login);

		            response.sendRedirect("/register/completelogin");
				}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			authentication.setAuthenticated(false);

            // Xóa thông tin đăng nhập
            SecurityContextHolder.getContext().setAuthentication(null);
       
		}	
	    
	   
	}
	
	

}
//OAuth2User user = (OAuth2User) authentication.getPrincipal();
//
//System.out.println("CustomOAuth2UserService invoked");
//// Lấy thông tin từ oauth2User
//String email = user.getAttribute("email");
//System.out.println(email);
//// Kiểm tra xem người dùng đã tồn tại trong cơ sở dữ liệu chưa
////   User user2 = userService.findByEmailAndProvider(email, Provider.GOOGLE); // Thay Provider.GOOGLE bằng giá trị tương ứng
//	User dangnhap = userService.findByEmail(email);
//	if (dangnhap != null) {
//		Provider pro = dangnhap.getProvider();
//		if (dangnhap.getProvider().equals(pro)) {
//			response.sendRedirect("/");
//		}else {
//			dangnhap.setProvider(Provider.GOOGLE);
//			dangnhap.setFirstname(user.getAttribute("given_name"));
//			userService.update(dangnhap);
//		}
//		
//	}else {
//		System.out.println("dc,");
//		User make = new User();
//		make.setEmail(email);
//		System.out.println("mail :"+make.getEmail());
//		response.sendRedirect("/");
//		
//	}