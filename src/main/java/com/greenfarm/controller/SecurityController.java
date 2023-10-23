package com.greenfarm.controller;

import java.util.Arrays;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.greenfarm.dao.UserDAO;
import com.greenfarm.service.UserService;
import com.greenfarm.service.impl.UserServerImpl;

import jakarta.servlet.http.HttpSession;

@Controller
public class SecurityController {

	@Autowired
	UserServerImpl acipl;

	@Autowired
	PasswordEncoder passwordE;

	@Autowired
	UserDAO acdao;

	@Autowired
	private UserService userService;

	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("message", "Vui lòng đăng nhập!");
		return "security/login";
	}

	@RequestMapping("/logoff")
	public String logoff(Model model) {
		model.addAttribute("message", "Bạn đã đăng xuất!");
		return "security/login";
	}
	
//	@GetMapping("/index/login/success")
//	public String login_success(Model model) {
//		model.addAttribute("message", "Đăng nhập thành công!");
//		return "security/login";
//	}
//
//	@GetMapping("/index/login/error")
//	public String login_error(Model model) {
//		model.addAttribute("message", "Sai thông tin đăng nhập!");
//		return "security/login";
//	}
//
//	@GetMapping("/index/login/unauthoried")
//	public String login_unauthored(Model model) {
//		model.addAttribute("message", "Không có quyền truy xuất!");
//		return "security/login";
//	}

	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("message", "Vui lòng đăng ký!");
		return "security/register";
	}

//
//	@GetMapping("/index/register/save")
//	public String registersave(Model model, @ModelAttribute Account account, HttpSession session) {
//		Account newacc = account;
//		newacc.setPassword(passwordE.encode(account.getPassword()));
//		Account accountcreate = accountService.create(newacc);
//
//		if (accountcreate != null) {
//			session.setAttribute("msg", "Register successfully");
//
//		} else {
//			// System.out.println("error in server");
//			session.setAttribute("msg", "Something wrong server");
//		}
//		return "security/register";
//	}
//
//	@GetMapping("/index/forgot")
//	public String forgot(Model model) {
//		return "security/forgotpassword";
//	}
//
//	@GetMapping("/index/logoff")
//	public String logoff(Model model) {
//		model.addAttribute("message", "Bạn đã đăng xuất!");
//		return "security/login";
//	}
//
//	@GetMapping("/index/logoff/success")
//	public String logoff_success(Model model) {
//		model.addAttribute("message", "Bạn đã đăng xuất!");
//		return "security/login";
//	}
//
//	@GetMapping("/index/profile")
//	public String profile(Model model) {
//
//		// Lấy thông tin người dùng đã xác thực từ SecurityContextHolder
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//		// Kiểm tra nếu người dùng đã xác thực
//		if (authentication.isAuthenticated()) {
//			// Lấy tên người dùng
//			String username = authentication.getName();
//
//			// Lấy các quyền (roles) của người dùng
//			String roles = authentication.getAuthorities().toString();
//
//			// Trả về thông tin tài khoản trong phản hồi
//			System.out.println("Xin chào, " + username + "! Bạn có các quyền: " + roles);
//		} else {
//			System.out.println("Xin chào! Bạn chưa đăng nhập.");
//		}
//		String username = authentication.getName();
//		Account ac = acdao.findByUsername(username);
//		System.out.println(ac.getUsername());
//		System.out.println(ac.getAddress());
//		System.out.println(ac.getFullname());
//		System.out.println(ac.getPassword());
//		System.out.println(ac.getPhone());
//		System.out.println(ac.getPhoto());
//		System.out.println(
//				"*************************************************************************?///////////////////////");
//		System.out
//				.println(ac.getAccountroles().stream().map(er -> er.getRole().getName()).collect(Collectors.toList()));
//		Role role = new Role();
//		role.setId(1);
//		System.out.println("//////////////////");
//		System.out.println(role.getName());
//
//		model.addAttribute("ac", ac);
//
//		return "/user/profile";
//	}
//
	@RequestMapping("/oauth2/login/form")
	public String fbform() {
		return "security/login";
	}

	@RequestMapping("/oauth2/login/error")
	public String fber() {
		return "security/login";
	}
//
//	@RequestMapping("/oauth2/login/success")
//	public String fbsuccess(OAuth2AuthenticationToken oauth2) {
//		acipl.loginFormOAuth2(oauth2);
//		return "forward:/index/login/success";
//	}
//
//	@RequestMapping("/login/oauth2/code/google")
//	public String ggform() {
//		return "security/login";
//	}

}
