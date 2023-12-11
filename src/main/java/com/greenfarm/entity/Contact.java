package com.greenfarm.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfarm.dto.Provider;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contacts")
public class Contact {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Positive(message = "Id tài khoản phải lớn hơn 0")
	private Integer contactid;
	
	@Column(name = "Email", unique = true)
	@Pattern(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$", message = "Email phải đúng định dạng.")
	@NotBlank(message = "Email là bắt buộc")
	@Email(message = "Email không hợp lệ!")
	private String email;
	
	@NotBlank(message = "Tên đầu là bắt buộc")
	private String fullname;
	
	@Column(unique = true)
	@NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại không hợp lệ")
	private String phonenumber;
	
	@NotBlank(message = "Nội dung không được để trống")
	private String content;
	
	@Temporal(TemporalType.DATE)
	private Date createddate = new Date();
}
