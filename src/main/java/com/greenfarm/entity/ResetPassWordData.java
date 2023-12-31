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

	
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,16}$\n", message = "Mật khẩu phải có từ 8 đến 16 ký tự, phải bao gồm ít nhất 1 chữ viết hoa và 1 số.")
	@NotEmpty(message = "Nhập mật khẩu mới")
	private String newpass;
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,16}$\n", message = "Mật khẩu phải có từ 8 đến 16 ký tự, phải bao gồm ít nhất 1 chữ viết hoa và 1 số.")
	@NotEmpty(message = "xác nhận lại mật khẩu mới")
	private String confirmpass;
	
	private String token;
}
