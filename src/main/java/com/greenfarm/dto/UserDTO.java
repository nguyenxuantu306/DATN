package com.greenfarm.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.greenfarm.entity.Address;
import com.greenfarm.entity.Comment;
import com.greenfarm.entity.User;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	
	@Positive(message = "Id tài khoản phải lớn hơn 0")
	private Integer userid;
	
	@NotBlank(message = "Email là bắt buộc")
	private String email;

	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,100}$", message = "Mật khẩu phải có từ 8 đến 16 ký tự, phải bao gồm ít nhất 1 chữ viết hoa và 1 số.")
	@NotEmpty(message = "Vui lòng nhập mật khẩu")
	private String password;

	@NotEmpty(message = "Thiếu mật khẩu xác nhận")
	private String repeatpassword;

	@NotBlank(message = "Tên đầu là bắt buộc")
	private String firstname;
	
	@NotBlank(message = "Tên cuối là bắt buộc")
	private String lastname;
	
	@NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại không hợp lệ")
	private String phonenumber;

	//@NotBlank(message = "Ảnh đại diện là bắt buộc")
	private String image; 
	
//	@NotBlank(message = "Địa chỉ là bắt buộc")
//	@Size(min = 5, max = 255, message = "Địa chỉ phải có từ 6 đến 255 ký tự")
//	private String address;
	
	@NotNull(message = "Giới tính phải được chọn")
	private Boolean gender;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Past(message = "Ngày sinh phải là một ngày trong quá khứ")
	private Date birthday;

	@Past(message = "Ngày tạo phải trước ngày hiện tại")
	@Temporal(TemporalType.DATE)
	private Date createddate;
	
	private Boolean isdeleted = Boolean.FALSE;

	private List<Comment> comment;
	
	private List<Address> address;
	
}