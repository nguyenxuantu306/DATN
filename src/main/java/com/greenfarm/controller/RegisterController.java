
package com.greenfarm.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greenfarm.dto.Provider;
import com.greenfarm.dto.RegisterDTO;
import com.greenfarm.dto.RegisteroauthDTO;
import com.greenfarm.dto.UserDTO;
import com.greenfarm.entity.Role;
import com.greenfarm.entity.User;
import com.greenfarm.entity.UserRole;
import com.greenfarm.exception.InvalidTokenException;
import com.greenfarm.exception.UserAlreadyExistException;
import com.greenfarm.service.UserService;
import com.greenfarm.service.RoleService;
import com.greenfarm.service.UserRoleService;
import com.mysql.cj.util.StringUtils;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegisterController {

	private static final String REDIRECT_TOKEN_MISSING = "redirect:/login?missing";
	private static final String REDIRECT_TOKEN_INVALID = "redirect:/login?invalid";
	private static final String REDIRECT_TOKEN_SUCCESS = "redirect:/login?token";
	private static final String REDIRECT_REGISTER_SUCCESS = "redirect:/login?success";
	private static final String REDIRECT_REGISTER_ERROR_EMAIL = "redirect:/register?errorEmail";
	private static final String REDIRECT_REGISTER_ERROR_PHONE_NUMBER = "redirect:/register?errorPhoneNumber";

	@Autowired
	com.greenfarm.service.UserService userservice;

	@Autowired
	UserRoleService userroleService;

	@Autowired
	RoleService roleService;

	@Autowired
	UserDetailsService detailsService;

	@Autowired
	AuthenticationManagerBuilder builder;

	@GetMapping
	public String registerUserq(Model model) {
		// , @ModelAttribute("userinfo") @Valid RegisterDTO userInfo
		model.addAttribute("userinfo", new RegisterDTO());

		/*
		 * if (bindingResult.hasErrors()) { // Nếu có lỗi từ dữ liệu người dùng, không
		 * cần kiểm tra tiếp và xử lý lỗi. model.addAttribute("registrationMsg",
		 * "Thông tin đăng ký không hợp lệ. Vui lòng kiểm tra lại.");
		 * System.out.println("lỗi này"); return "register"; } else if
		 * (!userInfo.getPassword().equals(userInfo.getRepeatpassword())) { // Nếu mật
		 * khẩu và xác nhận mật khẩu không khớp, thêm lỗi vào BindingResult.
		 * bindingResult.rejectValue("repeatpassword", "error.userDTO",
		 * "Mật khẩu và xác nhận mật khẩu không khớp"); }
		 */

		// Nếu không có lỗi, tiếp tục xử lý đăng ký tài khoản.
		// đăng ký
		// try {
		// // User user = Usermapper.INSTANCE.fromDto(userInfo);
		// User user = new User();
		// user.setFirstname(userInfo.getFirstname());
		// user.setLastname(userInfo.getLastname());
		// user.setEmail(userInfo.getEmail());
		// user.setPassword(userInfo.getPassword());
		// // user.setAddress(userInfo.getAddress());
		// user.setPhonenumber(userInfo.getPhonenumber());
		// // user.setBirthday(userInfo.getBirthday());
		// // user.setImage(userInfo.getImage());
		// // user.setGender(userInfo.getGender());
		// user.setCreateddate(new Date());
		// userservice.create(user);
		// } catch (Exception e) {
		// bindingResult.rejectValue("email", "error.userDTO", "An account already
		// exists for this email.");
		// model.addAttribute("registrationForm", userInfo);
		// return "register";
		// }
		// User userinfo = new User();
		// model.addAttribute("userinfo", userinfo);

		return "register";
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		binder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@PostMapping
	public String registerUser(Model model, @ModelAttribute("userinfo") @Valid RegisterDTO userInfo,
			BindingResult bindingResult) throws UserAlreadyExistException {

		if (bindingResult.hasErrors()) {
			// Nếu có lỗi từ dữ liệu người dùng, không cần kiểm tra tiếp và xử lý lỗi.
			model.addAttribute("error", "Thông tin đăng ký không hợp lệ. Vui lòng kiểm tra lại.");
			return "register";
		} else if (!userInfo.getPassword().equals(userInfo.getRepeatpassword())) {
			// Nếu mật khẩu và xác nhận mật khẩu không khớp, thêm lỗi vào BindingResult.
			bindingResult.rejectValue("repeatpassword", "error.userDTO", "Mật khẩu và xác nhận mật khẩu không khớp");
		} else if (userservice.findByPhonenumber(userInfo.getPhonenumber()) != null) {
			bindingResult.rejectValue("phonenumber", "error.userDTO", "Số điện thoại đã được sử dụng");
			return REDIRECT_REGISTER_ERROR_PHONE_NUMBER;
		}
		
		else {
			// Nếu không có lỗi, tiếp tục xử lý đăng ký tài khoản.
			// đăng ký
			try {
				// User user = Usermapper.INSTANCE.fromDto(userInfo);
				User user = new User();
				user.setFirstname(userInfo.getFirstname());
				user.setLastname(userInfo.getLastname());
				user.setEmail(userInfo.getEmail());
				user.setPassword(userInfo.getPassword());
				user.setAddress(null);
				user.setPhonenumber(userInfo.getPhonenumber());
				user.setBirthday(null);
				user.setImage(null);
				user.setGender(null);
				user.setCreateddate(new Date());
				userservice.create(user);
				try {
					User user2 = userservice.findByEmail(user.getEmail());
					Role role = roleService.findByid(2);
					UserRole userRole = new UserRole();
					userRole.setRole(role);
					userRole.setUser(user2);
					userroleService.create(userRole);
					List<UserRole> list = new ArrayList<>();
					list.add(userRole);
					user2.setUserRole(list);
					userservice.update(user2);
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("leu leu");
					e.printStackTrace();				}
				// them role
				
				return REDIRECT_REGISTER_SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
				bindingResult.rejectValue("email", "error.userDTO", "Email này đã tồn tại");
				model.addAttribute("registrationForm", userInfo);
				return REDIRECT_REGISTER_ERROR_EMAIL;
			}
		}
		return null;

		// Chuyển hướng tới trang thành công
		// model.addAttribute("registrationMsg",
		// messageSource.getMessage("user.registration.verification.email.msg", null,
		// LocaleContextHolder.getLocale()));

	}

	//
	// @GetMapping("/success")
	// public String registrationSuccess() {
	// // Hiển thị trang đăng ký thành công
	// return "success";
	// }

	@GetMapping("/completelogin")
	public String complete(Model model, Authentication authentication) {
		try {
			// Lấy giá trị từ RedirectAttributes

			OAuth2User auth2User = (OAuth2User) authentication.getPrincipal();
			System.out.println(auth2User);
			// Sử dụng giá trị trong model hoặc thực hiện các thao tác khác

			// Rest of your code
			if (authentication.isAuthenticated()) {
				RegisteroauthDTO dto = new RegisteroauthDTO();
				// OAuth2User auth2User = (OAuth2User) authentication.getPrincipal();
				System.out.println(auth2User);
				dto.setFirstname(auth2User.getAttribute("name"));
				dto.setLastname(auth2User.getAttribute("family_name"));
				dto.setEmail(auth2User.getAttribute("email"));
				//dto.setPassword(auth2User.getAttribute("nonce"));
				
				dto.setPhonenumber(null);
				model.addAttribute("userinfo", dto);
				return "registeroauth";
			} else {
				return "registeroauth";
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "registeroauth";
		}

	}
//@ModelAttribute("userinfo") @Valid RegisterDTO userInfo,
	
	@PostMapping("/completelogin")
	public String completelogin(Model model, @ModelAttribute("userinfo") @Valid RegisteroauthDTO userInfo,BindingResult bindingResult,
			Authentication authentication) {
		if (bindingResult.hasErrors()) {
			// Nếu có lỗi từ dữ liệu người dùng, không cần kiểm tra tiếp và xử lý lỗi.
			model.addAttribute("error", "Thông tin đăng ký không hợp lệ. Vui lòng kiểm tra lại.");
			return "registeroauth";
		}else if (userservice.findByEmail(userInfo.getEmail()) != null) {
			bindingResult.rejectValue("email", "error.userDTO", "Email này đã tồn tại");
			return REDIRECT_REGISTER_ERROR_EMAIL;
		}
		
		else if (userservice.findByPhonenumber(userInfo.getPhonenumber()) != null) {
			bindingResult.rejectValue("phonenumber", "error.userDTO", "Số điện thoại đã tồn tại");
			return REDIRECT_REGISTER_ERROR_PHONE_NUMBER;
		}
		else {
			
		
		 String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
	      
		try {
			// User user = Usermapper.INSTANCE.fromDto(userInfo);
			if (authentication.getPrincipal() instanceof OAuth2User) {
				OAuth2User auth2User = (OAuth2User) authentication.getPrincipal();
				User user = new User();
				user.setFirstname(userInfo.getFirstname());
				user.setLastname(userInfo.getLastname());
				user.setEmail(userInfo.getEmail());
				user.setPhonenumber(userInfo.getPhonenumber());

				
				
				 
				if (provider.equals("google")) {
					user.setPassword(auth2User.getAttribute("nonce"));
					user.setProvider(Provider.GOOGLE);
				}
			 else if (provider.equals("facebook")) {
				 user.setPassword("123456aS");
				 user.setProvider(Provider.FACEBOOK);
				}
				
				
				user.setAccountVerified(true);

				// user.setUserRole();
				user.setCreateddate(new Date());
				userservice.save(user);
				// them role
				Role role = roleService.findByid(2);
				UserRole userRole = new UserRole();
				userRole.setRole(role);
				userRole.setUser(user);
				userroleService.create(userRole);
				List<UserRole> list = new ArrayList<>();
				list.add(userRole);
				user.setUserRole(list);
				userservice.update(user);

				try {
					UserDetails userDetails = detailsService.loadUserByUsername(user.getEmail());
					// Tạo một đối tượng Authentication
					List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));

					Authentication authentication2 = new UsernamePasswordAuthenticationToken(userDetails, null,
							authorities);

					// Đặt thông tin xác thực vào SecurityContext
					SecurityContext sc = SecurityContextHolder.getContext();
					sc.setAuthentication(authentication2);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					return "redirect:/login";
				}
				return "redirect:/";
			} else {
				return "redirect:/login";
			}
		} catch (Exception e) {
			e.printStackTrace();
			// bindingResult.rejectValue("email", "error.userDTO", "An account already
			// exists for this email.");
			model.addAttribute("registrationForm", userInfo);
			return "registeroauth";
		}
		}

	}

	@GetMapping("/verify")
	public String verifyCustomer(@RequestParam(required = false) String token, final Model model,
			RedirectAttributes redirAttr) {

		if (StringUtils.isEmptyOrWhitespaceOnly(token)) {
			// redirAttr.addFlashAttribute("tokenError",
			// messageSource.getMessage("user.registration.verification.missing.token",
			// null,LocaleContextHolder.getLocale()));
			return REDIRECT_TOKEN_MISSING;
		}
		try {
			userservice.verifyUser(token);
		} catch (InvalidTokenException e) {
			// redirAttr.addFlashAttribute("tokenError",
			// messageSource.getMessage("user.registration.verification.invalid.token",
			// null,LocaleContextHolder.getLocale()));
			return REDIRECT_TOKEN_INVALID;
		}

		// redirAttr.addFlashAttribute("verifiedAccountMsg",
		// messageSource.getMessage("user.registration.verification.success",
		// null,LocaleContextHolder.getLocale()));
		return REDIRECT_TOKEN_SUCCESS;
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		Collection<? extends GrantedAuthority> mapRoles = roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
		return mapRoles;
	}

}