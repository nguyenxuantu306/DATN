package com.greenfarm.controller;

import java.security.Principal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greenfarm.dao.UserDAO;
import com.greenfarm.dto.UserDTO;
import com.greenfarm.entity.Passworddata;
import com.greenfarm.entity.ResetPassWordData;
import com.greenfarm.entity.User;
import com.greenfarm.exception.InvalidTokenException;
import com.greenfarm.exception.UnkownIdentifierException;
import com.greenfarm.service.UserService;

import jakarta.validation.Valid;

@Controller
public class SecurityController {

	@Autowired
	PasswordEncoder passwordE;

	@Autowired
	UserDAO acdao;

	@Autowired
	private UserService userService;

	// @ModelAttribute
	// public void commonUser(Principal p,Model model) {
	// if (p != null) {
	// String email = p.getName();
	// com.greenfarm.entity.User user = userService.findByEmail(email);
	// model.addAttribute("user", user);
	// }
	// }

	@RequestMapping("/login")
	public String login(Model model) {
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
	@RequestMapping("/oauth2/login/form")
	public String fbform() {
		return "security/login";
	}

	@RequestMapping("/oauth2/login/error")
	public String fber() {
		return "security/login";
	}

	@RequestMapping("/oauth2/login/success")
	public String fbsuccess(OAuth2AuthenticationToken oauth2) {
		userService.loginFormOAuth2(oauth2);
		return "redirect:/";
	}

	@RequestMapping("/login/oauth2/code/google")
	public String ggform() {
		return "security/login";
	}

	@GetMapping("/profile")
	public String profile(Model model) {
		// Lấy thông tin người dùng đã xác thực từ SecurityContextHolder
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User userchange = new User();
		model.addAttribute("userchange", userchange);

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

	@PostMapping("/profile")
	public String profileupdate(Model model, @ModelAttribute("userchange") @Valid User userchange,
			BindingResult bindingResult) {
		// if (bindingResult.hasErrors()) {
		// // Nếu có lỗi từ dữ liệu người dùng, không cần kiểm tra tiếp và xử lý lỗi.
		// model.addAttribute("error", "Thông tin đăng ký không hợp lệ. Vui lòng kiểm
		// tra lại.");
		// return "register";
		// }
		// Lấy thông tin người dùng đã xác thực từ SecurityContextHolder
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// Kiểm tra nếu người dùng đã xác thực
		if (authentication.isAuthenticated()) {
			// Lấy tên người dùng
			String username = authentication.getName();
			User user = userService.findByEmail(username);

			user.setAddress(userchange.getAddress());
			user.setImage(userchange.getImage());
//			user.setBirthday(userchange.getBirthday()); 
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
			model.addAttribute("currentPasswordError", "Mật khẩu hiện tại không đúng.");
			return "security/changepass";
		}

		if (!userService.isPasswordMatching(passchange.getNewpass(), passchange.getConfirmpass())) {
			model.addAttribute("newPasswordError", "Mật khẩu mới và xác nhận mật khẩu không khớp.");
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
	public String fogot(@ModelAttribute User user, Model model, @RequestParam String email) throws UnkownIdentifierException {
		try {
			System.out.println(email);
			// String userName = email;
			userService.forgottenPassword(email);
		} catch (Exception e) {
			// TODO: handle exception
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

}
