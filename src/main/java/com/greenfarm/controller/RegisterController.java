
package com.greenfarm.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
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
import com.greenfarm.entity.Role;
import com.greenfarm.entity.User;
import com.greenfarm.entity.UserRole;
import com.greenfarm.exception.InvalidTokenException;
import com.greenfarm.exception.UserAlreadyExistException;
import com.greenfarm.service.RoleService;
import com.greenfarm.service.UserRoleService;
import com.greenfarm.service.UserService;
import com.mysql.cj.util.StringUtils;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegisterController {

	private static final String REDIRECT_LOGIN = "redirect:/login";
	
	
	@Autowired
	UserRoleService userroleService;
	@Autowired
	RoleService roleService;
	@Autowired
	UserDetailsService detailsService;
	@Autowired
	AuthenticationManagerBuilder builder;
	
	@Autowired
	UserService userservice;

	@GetMapping
	public String registerUserq(Model model) {

		model.addAttribute("userinfo", new RegisterDTO());

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
			System.out.println("pas");
			return "register";
		} else if (!userInfo.getPassword().equals(userInfo.getRepeatpassword())) {
			// Nếu mật khẩu và xác nhận mật khẩu không khớp, thêm lỗi vào BindingResult.
			System.out.println("repass");
			bindingResult.rejectValue("repeatpassword", "error.userDTO", "Mật khẩu và xác nhận mật khẩu không khớp");
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
				System.out.println("chay toi impl");
				userservice.create(user);
				return "redirect:/login";
			} catch (Exception e) {
				e.printStackTrace();
				bindingResult.rejectValue("email", "error.userDTO", "An account already exists for this email.");
				System.out.println("mail");
				model.addAttribute("registrationForm", userInfo);
				return "register";
			}
		}
		return null;

		// Nếu không có lỗi, tiếp tục xử lý đăng ký tài khoản.
		// đăng ký
//		try {
//			// User user = Usermapper.INSTANCE.fromDto(userInfo);
//			User user = new User();
//			user.setFirstname(userInfo.getFirstname());
//			user.setLastname(userInfo.getLastname());
//			user.setEmail(userInfo.getEmail());
//			user.setPassword(userInfo.getPassword());
//			user.setAddress(null);
//			user.setPhonenumber(userInfo.getPhonenumber());
//			user.setBirthday(null);
//			user.setImage(null);
//			user.setGender(null);
//			user.setCreateddate(new Date());
//			System.out.println("chay toi impl");
//			userservice.create(user);
//			return "redirect:/login";
//		} catch (Exception e) {
//			e.printStackTrace();
//			bindingResult.rejectValue("email", "error.userDTO", "An account already exists for this email.");
//			System.out.println("mail");
//			model.addAttribute("registrationForm", userInfo);
//			return "register";
//		}
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
				// dto.setPassword(auth2User.getAttribute("nonce"));

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
	// @ModelAttribute("userinfo") @Valid RegisterDTO userInfo,

	@PostMapping("/completelogin")
	public String completelogin(Model model, @ModelAttribute("userinfo") @Valid RegisteroauthDTO userInfo,
			BindingResult bindingResult, Authentication authentication) {
		if (bindingResult.hasErrors()) {
			// Nếu có lỗi từ dữ liệu người dùng, không cần kiểm tra tiếp và xử lý lỗi.
			model.addAttribute("error", "Thông tin đăng ký không hợp lệ. Vui lòng kiểm tra lại.");

			return "registeroauth";
		} else if (userservice.findByEmail(userInfo.getEmail()) != null) {

			bindingResult.rejectValue("email", "error.userDTO", "Đã có tài khoản sử dụng email này");
			return "registeroauth";
		}

		else if (userservice.findByPhonenumber(userInfo.getPhonenumber()) != null) {
			System.out.println("phonenumber");
			bindingResult.rejectValue("phonenumber", "error.userDTO", "so dien thoai da ton tai");
			return "registeroauth";
		} else {

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
					} else if (provider.equals("facebook")) {
						user.setPassword("123456aS");
						user.setProvider(Provider.FACEBOOK);
					}

					user.setAccountVerified(true);

					// user.setUserRole();
					user.setCreateddate(new Date());
					userservice.save(user);
					// them role
					Role role = roleService.findByid(2);
					System.out.println("Role : " + role);
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
						System.out.println("userdetaile>.........");
						System.out.println(userDetails);
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
				System.out.println("lôi");
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
			System.out.println("missing token");
			return REDIRECT_LOGIN;
		}
		try {
			userservice.verifyUser(token);
		} catch (InvalidTokenException e) {
			// redirAttr.addFlashAttribute("tokenError",
			// messageSource.getMessage("user.registration.verification.invalid.token",
			// null,LocaleContextHolder.getLocale()));
			System.out.println("inatokenlid ");
			return REDIRECT_LOGIN;
		}

		// redirAttr.addFlashAttribute("verifiedAccountMsg",
		// messageSource.getMessage("user.registration.verification.success",
		// null,LocaleContextHolder.getLocale()));
		System.out.println("thanh cong");
		return REDIRECT_LOGIN;
	}

}