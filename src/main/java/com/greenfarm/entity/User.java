package com.greenfarm.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Positive(message = "IdUser phải lớn hơn 0")
	Integer userid;

	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,16}$", message = "Mật khẩu phải có từ 8 đến 16 ký tự, phải bao gồm ít nhất 1 chữ viết hoa và 1 số.")
	@NotEmpty(message = "Thiếu password")
	String password;

	@Column(unique = true)
	@Pattern(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$", message = "Email phải đúng định dạng.")
	@NotBlank(message = "Email là bắt buộc")
	@Email(message = "Email không hợp lệ!")
	String email;

	@NotBlank(message = "Mô tả là bắt buộc")
	String firstname;

	@NotBlank(message = "Mô tả là bắt buộc")
	String lastname;

	/*
	 * @Pattern(regexp = "^[1-9][0-9]*$", message =
	 * "Số điện thoại phải là số nguyên dương và không chứa ký tự khác")
	 */
	String phonenumber;

	@NotBlank(message = "Ảnh đại diện là bắt buộc")
	String image;

	@NotBlank(message = "Địa chỉ là bắt buộc")
	String address;

	@NotNull(message = "Giới tính là bắt buộc")
	Boolean gender;

	@Past(message = "Ngày sinh phải trước ngày hiện tại")
	@Temporal(TemporalType.DATE)
	Date birthday = new Date();

	@Temporal(TemporalType.DATE)
	Date createddate = new Date();

	// Boolean IsActive;

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

}