package com.greenfarm.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfarm.dto.Provider;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Positive(message = "Id tài khoản phải lớn hơn 0")
	private Integer userid;

	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,100}$", message = "Mật khẩu phải có từ 8 đến 16 ký tự, phải bao gồm ít nhất 1 chữ viết hoa và 1 số.")
	@NotEmpty(message = "Vui lòng nhập mật khẩu")
	String password;

	@Column(name = "Email", unique = true)
	@Pattern(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$", message = "Email phải đúng định dạng.")
	@NotBlank(message = "Email là bắt buộc")
	@Email(message = "Email không hợp lệ!")
	String email;

	@NotBlank(message = "Tên đầu là bắt buộc")
	String firstname;

	@NotBlank(message = "Tên cuối là bắt buộc")
	String lastname;

	@Column(unique = true)
	@NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại không hợp lệ")
	String phonenumber;

// @NotBlank(message = "Ảnh đại diện là bắt buộc")
	String image;

//	@NotBlank(message = "Địa chỉ là bắt buộc")
//	@Size(min = 5, max = 255, message = "Địa chỉ phải có từ 6 đến 255 ký tự")
	String Address;

//	@NotNull(message = "Giới tính phải được chọn")
	Boolean gender;

	
//	@DateTimeFormat(pattern = "yyyy-MM-dd")
//	@Past(message = "Ngày sinh phải là một ngày trong quá khứ")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;

//	@Past(message = "Ngày tạo phải trước ngày hiện tại")
	@Temporal(TemporalType.DATE)
	Date createddate = new Date();

	private Boolean isdeleted = Boolean.FALSE;
	// Boolean IsActive;

	@JsonIgnore
	@OneToMany( mappedBy = "user")
	List<Address> address;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Booking> booking;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Comment> comment;

	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	List<UserRole> userRole;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Tour> tour;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Order> order;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<UserDiscount> discount;

	private boolean accountVerified;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	private Set<Securetoken> tokens;

	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	List<Review> Review;

	@Enumerated(EnumType.STRING)
	private Provider provider;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<VoucherUser> voucheruser;

	public void setIsDeleted(boolean isdeleted) {
		this.isdeleted = isdeleted;
	}
}
