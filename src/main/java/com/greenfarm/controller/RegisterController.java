
package com.greenfarm.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
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

import com.greenfarm.dto.RegisterDTO;
import com.greenfarm.entity.User;
import com.greenfarm.exception.InvalidTokenException;
import com.greenfarm.exception.UserAlreadyExistException;
import com.greenfarm.service.UserService;
import com.mysql.cj.util.StringUtils;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegisterController {

	private static final String REDIRECT_TOKEN_MISSING = "redirect:/login?missing";
	private static final String REDIRECT_TOKEN_INVALID = "redirect:/login?invalid";
	private static final String REDIRECT_TOKEN_SUCCESS = "redirect:/login?token";
	private static final String REDIRECT_REGISTER_SUCCESS = "redirect:/login?success";

	@Autowired
	UserService userservice;

	@GetMapping
	public String registerUserq(Model model, @ModelAttribute("userinfo") RegisterDTO userInfo) {
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
			System.out.println("phonenumber");
			bindingResult.rejectValue("phonenumber", "error.userDTO", "Số điện thoại đã được sử dụng");
		}
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
			return REDIRECT_REGISTER_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			bindingResult.rejectValue("email", "error.userDTO", "Email này đã tồn tại!");
			model.addAttribute("registrationForm", userInfo);
			return "register";
		}
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

	@GetMapping("/verify")
	public String verifyCustomer(@RequestParam(required = false) String token, final Model model,
			RedirectAttributes redirAttr) {

		if (StringUtils.isEmptyOrWhitespaceOnly(token)) {
			// redirAttr.addFlashAttribute("tokenError",
			// messageSource.getMessage("user.registration.verification.missing.token",
			// null,LocaleContextHolder.getLocale()));
			System.out.println("missing token");
			return REDIRECT_TOKEN_MISSING;
		}
		try {
			userservice.verifyUser(token);
		} catch (InvalidTokenException e) {
			// redirAttr.addFlashAttribute("tokenError",
			// messageSource.getMessage("user.registration.verification.invalid.token",
			// null,LocaleContextHolder.getLocale()));
			System.out.println("inatokenlid ");
			return REDIRECT_TOKEN_INVALID;
		}

		// redirAttr.addFlashAttribute("verifiedAccountMsg",
		// messageSource.getMessage("user.registration.verification.success",
		// null,LocaleContextHolder.getLocale()));
		System.out.println("thanh cong");
		return REDIRECT_TOKEN_SUCCESS;
	}

}