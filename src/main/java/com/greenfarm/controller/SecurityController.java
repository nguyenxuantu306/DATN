package com.greenfarm.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.util.Value;
import com.greenfarm.cloudinary.CloudinaryService;
import com.greenfarm.config.CustomOAuth2successHandler;
import com.greenfarm.dao.UserDAO;
import com.greenfarm.dto.RegisterDTO;
import com.greenfarm.entity.Passworddata;
import com.greenfarm.entity.ResetPassWordData;
import com.greenfarm.entity.User;
import com.greenfarm.exception.InvalidTokenException;
import com.greenfarm.exception.UnkownIdentifierException;
import com.greenfarm.exception.UserAlreadyExistException;
import com.greenfarm.service.UserService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class SecurityController {

	@Autowired
	CustomOAuth2successHandler auth2successHandler;

	@Autowired
	PasswordEncoder passwordE;

	@Autowired
	UserDAO acdao;

	@Autowired
	private UserService userService;

	@Autowired
	private CloudinaryService cloudinaryService;

	// @ModelAttribute
	// public void commonUser(Principal p,Model model) {
	// if (p != null) {
	// String email = p.getName();
	// com.greenfarm.entity.User user = userService.findByEmail(email);
	// model.addAttribute("user", user);
	// }
	// }

	// HÀM VALIDATION
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		binder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("message", "Vui lòng đăng nhập!");
		return "security/login";
	}

	@PostMapping("/login")
	public String login1(Model model) {
		model.addAttribute("message", "Vui lòng đăng nhập!");
		return "security/login";
	}

	@RequestMapping("/logoff")
	public String logoff(Model model) {
		model.addAttribute("message", "Bạn đã đăng xuất!");
		return "redirect:/";
	}

	// @GetMapping("/index/login/success")
	// public String login_success(Model model) {
	// model.addAttribute("message", "Đăng nhập thành công!");
	// return "security/login";
	// }
	//
	// @GetMapping("/index/login/error")
	// public String login_error(Model model) {
	// model.addAttribute("message", "Sai thông tin đăng nhập!");
	// return "security/login";
	// }
	//
	// @GetMapping("/index/login/unauthoried")
	// public String login_unauthored(Model model) {
	// model.addAttribute("message", "Không có quyền truy xuất!");
	// return "security/login";
	// }

	//
	// @GetMapping("/index/register/save")
	// public String registersave(Model model, @ModelAttribute Account account,
	// HttpSession session) {
	// Account newacc = account;
	// newacc.setPassword(passwordE.encode(account.getPassword()));
	// Account accountcreate = accountService.create(newacc);
	//
	// if (accountcreate != null) {
	// session.setAttribute("msg", "Register successfully");
	//
	// } else {
	// // System.out.println("error in server");
	// session.setAttribute("msg", "Something wrong server");
	// }
	// return "security/register";
	// }
	//

	//
	// @GetMapping("/index/logoff")
	// public String logoff(Model model) {
	// model.addAttribute("message", "Bạn đã đăng xuất!");
	// return "security/login";
	// }
	//
	// @GetMapping("/index/logoff/success")
	// public String logoff_success(Model model) {
	// model.addAttribute("message", "Bạn đã đăng xuất!");
	// return "security/login";
	// }
	//

	//
//	@RequestMapping("/oauth2/login/form")
//	public String fbform1() {
//		return "security/login";
//	}

	@RequestMapping("/oauth2/login/error")
	public String fber() {
		return "security/login";
	}

	@RequestMapping("/oauth2/login/success")
	public String fbsuccess(OAuth2AuthenticationToken oauth2, Authentication authentication) {
		System.out.println("day la thong tin oauth2 " + oauth2);

		if (authentication != null) {
			OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
			String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
			// provider là tên đăng ký của nhà cung cấp OAuth2 (ví dụ: "google", "facebook")
			return "Logged in via " + provider;
		} else {
			return "Not logged in or unknown authentication provider";
		}
//	userService.loginFormOAuth2(oauth2);
		// return "redirect:/";
	}

//login/oauth2/authorization/facebook
	@RequestMapping("/oauth2/authorization/facebook")
	public String fbform() {
		return "security/login";
	}

	@RequestMapping("/login/oauth2/code/google")
	public String ggform() {
		return "security/login";
	}

	@GetMapping("/profile")
	public String profile(Model model) {
		// Lấy thông tin người dùng đã xác thực từ SecurityContextHolder
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// Kiểm tra nếu người dùng đã xác thực
		if (authentication.isAuthenticated()) {
			// Lấy tên người dùng
			String username = authentication.getName();
			User user = userService.findByEmail(username);
			model.addAttribute("user", user);
			// Lấy các quyền (roles) của người dùng
			String roles = authentication.getAuthorities().toString();

			model.addAttribute("roles", roles);
			// Trả về thông tin tài khoản trong phản hồi
			System.out.println("Xin chào, " + username + "! Bạn có các quyền: " + roles);
			return "profile";
		} else {
			System.out.println("Xin chào! Bạn chưa đăng nhập.");

			return "profile";
		}

		// return "redirect:/login";
	}

	@Autowired
	ServletContext app;
//	@Value("${upload.dir}")
//    private String uploadDir;  // Đường dẫn đến thư mục lưu trữ file, được cấu hình trong application.properties

	
	@PostMapping("/profile")
	public String profileupdate(Model model, @ModelAttribute("userchange") @Valid User userchange,
			BindingResult bindingResult, @RequestParam("attach") MultipartFile attach) throws IOException {
//		 if (bindingResult.hasErrors()) {
//		 // Nếu có lỗi từ dữ liệu người dùng, không cần kiểm tra tiếp và xử lý lỗi.
//			 model.addAttribute("error", "Thông tin đăng ký không hợp lệ. Vui lòng kiểm tra lại.");
//			 return "profile";
//		 }

		// Lấy thông tin người dùng đã xác thực từ SecurityContextHolder
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(authentication);
		// Kiểm tra nếu người dùng đã xác thực
		if (authentication.isAuthenticated()) {
			// Lấy tên người dùng
			String username = authentication.getName();
			User user = userService.findByEmail(username);
			System.out.println("*******");
			System.out.println(user.getPassword());

			// up image to cloud

			// user.setImage(new String(file.getBytes())); // Chuyển đổi dữ liệu của file
			// thành String và lưu vào entity
			if (!attach.isEmpty()) {
	            try {
	              
	                System.out.println("name"+attach.getOriginalFilename());
					System.out.println("type"+attach.getContentType());
					System.out.println("size"+attach.getSize());
					System.out.println("111111111111111111");
					/// chay den day lla noloi do no cu tim trong o dia C
					String imageUrl = cloudinaryService.uploadFile(attach);
					
					System.out.println("2222222222222222222222");
					System.out.println(imageUrl);
					user.setImage(imageUrl);
					System.out.println(userchange.getImage());
	                // Các bước xử lý khác...
	            } catch (Exception e) {
	                e.printStackTrace();
	                // Xử lý lỗi khi lưu file
	            }
	        }
				else if(attach.isEmpty()){
				user.setImage(user.getImage());
			}

			if (isAtLeast16YearsOld(userchange.getBirthday())) {
				user.setBirthday(userchange.getBirthday());
				System.out.println(user.getBirthday());
			}
			user.setFirstname(userchange.getFirstname());
			user.setLastname(userchange.getLastname());
			user.setPhonenumber(userchange.getPhonenumber());
			user.setGender(userchange.getGender());

			try {
				userService.update(user);
			} catch (Exception e) {
				e.printStackTrace();
			}

			model.addAttribute("user", user);
			String roles = authentication.getAuthorities().toString();
			model.addAttribute("roles", roles);
			return "profile";
		} else {
			System.out.println("Xin chào! Bạn chưa đăng nhập.");

		}
		return "profile";
	}

	@GetMapping("/changepass")
	public String changepass(Model model) {

		model.addAttribute("passchange", new Passworddata());
		return "security/changepass";
	}

	@PostMapping("/changepass")
	public String changepass1(Model model, @ModelAttribute("passchange") @Valid Passworddata passchange,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "security/changepass"; // Trả về trang thay đổi mật khẩu với thông báo lỗi
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByEmail(authentication.getName());

		if (!userService.iscurrentPasswordMatching(user, passchange.getCurrentpass())) {
			model.addAttribute("currentPasswordError", "Mật khẩu hiện tại không đúng");
			return "security/changepass";
		}

		if (!userService.isPasswordMatching(passchange.getNewpass(), passchange.getConfirmpass())) {
			model.addAttribute("newPasswordError", "Mật khẩu mới và mật khẩu xác nhận không khớp");
			return "security/changepass";
		}

		// Cập nhật mật khẩu trong cơ sở dữ liệu
		user.setPassword(passwordE.encode(passchange.getNewpass()));
		userService.update(user);
		return "redirect:/profile";
	}

	@GetMapping("/forgot")
	public String forgot(Model model) {
		return "security/forgetpassword";
	}

	@PostMapping("/forgot")
	public String fogot(@ModelAttribute User user, Model model, @RequestParam String email, BindingResult bindingResult)
			throws UserAlreadyExistException {

		if (bindingResult.hasErrors()) {
			// Nếu có lỗi từ dữ liệu người dùng, không cần kiểm tra tiếp và xử lý lỗi.
			model.addAttribute("error", "Thông tin đăng ký không hợp lệ. Vui lòng kiểm tra lại.");
			return "redirect:/login";
		}

		try {
			System.out.println(email);
			// String userName = email;
			userService.forgottenPassword(email);
		} catch (Exception e) {

		}

		return "redirect:/login";
	}

	@GetMapping("/resetpass")
	public String getressetPassword(@ModelAttribute("data") ResetPassWordData data,
			@RequestParam(required = false) String token, final Model model) {
		if (StringUtils.isEmpty(token)) {
			// redirAttr.addFlashAttribute("tokenError",final RedirectAttributes redirAttr,
			// messageSource.getMessage("user.registration.verification.missing.token",
			// null, LocaleContextHolder.getLocale())
			//
			System.out.println("Không tìm thấy token");
			return "redirect: /login";
		}
		// model.addAttribute("data", new ResetPassWordData());
		System.out.println("có token");
		ResetPassWordData datap = new ResetPassWordData();
		datap.setToken(token);
		setResetPasswordForm(model, datap);

		return "security/resetpass";
	}

	@PostMapping("/resetpass")
	public String resetPassword(@ModelAttribute("data") @Valid ResetPassWordData data, final Model model,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			// Nếu có lỗi từ dữ liệu người dùng, không cần kiểm tra tiếp và xử lý lỗi.
			model.addAttribute("error", "Thông tin đăng ký không hợp lệ. Vui lòng kiểm tra lại.");
			return "security/resetpass";
		}
		try {
			userService.updatePassword(data.getNewpass(), data.getToken());
			// customerAccountService.updatePassword(data.getPassword(), data.getToken());
		} catch (InvalidTokenException | UnkownIdentifierException e) {
			e.getStackTrace();
			// log error statement
			// model.addAttribute("tokenError",
			// messageSource.getMessage("user.registration.verification.invalid.token",
			// null, LocaleContextHolder.getLocale())
			// );
			System.out.println("Báo lỗi không thấy token");

			return "security/resetpass";
		}
		// model.addAttribute("passwordUpdateMsg",
		// messageSource.getMessage("user.password.updated.msg", null,
		// LocaleContextHolder.getLocale())
		// );
		System.out.println("Báo đã update");
		setResetPasswordForm(model, new ResetPassWordData());

		return "redirect:/login";
	}

	private void setResetPasswordForm(final Model model, ResetPassWordData data) {
		model.addAttribute("forgotPassword", data);
	}

	public boolean isAtLeast16YearsOld(Date birthday) {
		if (birthday == null) {
			// Xử lý trường hợp ngày sinh không được đặt
			return false;
		}

		// Chuyển đổi từ Date sang LocalDate
		LocalDate birthdate = new java.sql.Date(birthday.getTime()).toLocalDate();
		LocalDate currentDate = LocalDate.now();

		// Tính khoảng cách thời gian giữa ngày sinh và ngày hiện tại
		Period age = Period.between(birthdate, currentDate);

		// Kiểm tra xem tuổi có lớn hơn hoặc bằng 16 không
		return age.getYears() >= 16;
	}
}
