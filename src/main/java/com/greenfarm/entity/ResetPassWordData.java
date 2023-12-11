package com.greenfarm.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPassWordData {

	
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[\\W]).{8,16}$", message = "Mật khẩu phải có từ 8 đến 16 ký tự, phải bao gồm ít nhất 1 chữ hoa 1 chữ thường 1 số và 1 ký tự đặc biệt.")
	@NotEmpty(message = "Nhập mật khẩu mới")
	private String newpass;
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[\\W]).{8,16}$", message = "Mật khẩu phải có từ 8 đến 16 ký tự, phải bao gồm ít nhất 1 chữ hoa 1 chữ thường 1 số và 1 ký tự đặc biệt.")
	@NotEmpty(message = "xác nhận lại mật khẩu mới")
	private String confirmpass;
	
	private String token;
}
