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
public class ContactDTO {
	
	@Positive(message = "Id tài khoản phải lớn hơn 0")
	private Integer contactid;
	
	@NotBlank(message = "Email là bắt buộc")
	private String email;

	
	@NotBlank(message = "Tên là bắt buộc")
	private String fullname;
	
	
	@NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại không hợp lệ")
	private String phonenumber;

	@NotBlank(message = "Nội dung là bắt buộc")
	private String content; 
	

	@Past(message = "Ngày tạo phải trước ngày hiện tại")
	@Temporal(TemporalType.DATE)
	private Date createddate;

}