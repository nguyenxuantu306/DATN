package com.greenfarm.dto;
import java.util.Date;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	//@NotEmpty(message = "UserId không được để trống")
	private Integer userid;
	@NotEmpty(message = "Thiếu Email")
	@Email(message = "Email không hợp lệ")
	private String email;
	
	@NotEmpty(message = "Thiếu password")
	private String password;
	
	@NotEmpty
	private String repeatpassword;
	
	@NotEmpty(message = "Thiếu FirstName")
	private String firstname;
	@NotEmpty(message = "Thiếu LastName")
	private String lastname;
	@NotEmpty(message = "Số điện thoại không được để trống")
	@Pattern(regexp = "^\\d{10}$", message = "Số điện thoại phải chứa đúng 10 chữ số")
	private String phonenumber;
	
	private String image;
	@NotEmpty(message = "Thiếu Address")
	private String address;
	@NotNull(message = "Gender không được để trống")
    @AssertTrue(message = "Trường Gender phải là true")
	private Boolean gender;
	@NotNull(message = "Trường Birthday không được để trống")
	@Past(message = "Birthday phải ở trong quá khứ")
	private Date birthday;
//	@NotNull(message = "Trường CreatedDate không được để trống")
//	@PastOrPresent(message = "CreatedDate phải ở trong quá khứ hoặc hiện tại mới tạo")
	private Date createddate;
}