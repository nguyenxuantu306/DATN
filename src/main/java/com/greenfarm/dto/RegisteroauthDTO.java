
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
public class RegisteroauthDTO {

	private Integer userid;
	@NotEmpty(message = "Thiếu Email")
	@Email(message = "Email không hợp lệ")
	private String email;
	private String password;
	
	@NotEmpty(message = "Thiếu FirstName")
	private String firstname;
	@NotEmpty(message = "Thiếu LastName")
	private String lastname;
	@NotEmpty(message = "Số điện thoại không được để trống")
	@Pattern(regexp = "^\\d{10}$", message = "Số điện thoại phải chứa đúng 10 chữ số")
	private String phonenumber;
	
	
	private Date createddate;
}