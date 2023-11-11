package com.greenfarm.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Products")
public class Product implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer productid;

	@NotBlank(message = "Bạn chưa nhập tên rau")
	@Size(max = 255, message = "Tên rau phải ít hơn 255 ký tự")
	private String productname;

	@NotBlank(message = "Mô tả rau không được để trống")
	@Size(max = 1000, message = "Mô tả phải ít hơn 1000 ký tự")
	private String Description;

	@NotNull(message = "Bạn chưa nhập giá rau")
	@Positive(message = "Giá phải lớn hơn 0")
	private Float price;

	private String image;

	@NotNull(message = "Số lượng rau là bắt buộc")
	@Positive(message = "Số lượng phải lớn hơn 0 ")
	private Integer quantityavailable;

	
	
	@ManyToOne
	@JoinColumn(name = "categoryid")
	Category category;

	@JsonIgnore
	@OneToMany(mappedBy = "product")
	List<OrderDetail> orderDetails;

	@JsonIgnore
	@OneToMany(mappedBy = "product")
	List<ProductImage> productimage;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER,mappedBy = "product")
	List<Review> Review;
}
