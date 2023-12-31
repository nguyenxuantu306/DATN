package com.greenfarm.dto;

import java.util.Date;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {

	private Integer userid;
	
	@NotEmpty(message = "Email không được để trống")
	@Pattern(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$", message = "Email phải đúng định dạng.")
	private String email;
	
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9]).{8,16}$", message = "Mật khẩu phải có từ 8 đến 16 ký tự, phải bao gồm ít nhất 1 chữ viết hoa và 1 số.")
	@NotEmpty(message = "Mật khẩu không được để trống")
	private String password;
	
	@NotEmpty(message = "Mật khẩu xác nận không được để trống")
	private String repeatpassword;
	
	@NotEmpty(message = "Họ và tên đệm không được để trống")
	private String firstname;
	@NotEmpty(message = "Tên không được để trống")
	private String lastname;
	@NotEmpty(message = "Số điện thoại không được để trống")
	@Pattern(regexp = "^\\d{10}$", message = "Số điện thoại phải chứa đúng 10 chữ số")
	private String phonenumber;
	
	private Date createddate;
}
