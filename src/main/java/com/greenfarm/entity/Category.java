package com.greenfarm.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "productcategories")
public class Category implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Integer categoryid;

	@NotBlank(message = "Tên loại rau là bắt buộc")
	@Size(max = 255, message = "Tên loại rau phải ít hơn 255 ký tự")
	private String categoryname;

	@NotBlank(message = "Mô tả là bắt buộc")
	@Size(max = 1000, message = "Mô tả phải ít hơn 1000 ký tự")
	private String descriptions;

	@JsonIgnore
	@OneToMany(mappedBy = "category")
	List<Product> products;

}
