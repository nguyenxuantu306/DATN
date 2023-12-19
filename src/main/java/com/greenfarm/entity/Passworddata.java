package com.greenfarm.entity;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passworddata {
	
	@NotEmpty(message = "Hãy nhập mật khẩu cũ")
	private String currentpass;
	
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,16}$", message = "Mật khẩu phải từ 8 đến 16 ký tự bao gồm ít nhất 1 chữ viết hoa và 1 số")
	@NotEmpty(message = "Nhập mật khẩu mới")
	private String newpass;
	
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,16}$", message = "Mật khẩu phải từ 8 đến 16 ký tự bao gồm ít nhất 1 chữ viết hoa và 1 số")
	@NotEmpty(message = "Vui lòng xác nhận lại mật khẩu mới")
	private String confirmpass;
}
