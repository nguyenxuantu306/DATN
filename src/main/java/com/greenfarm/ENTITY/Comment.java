package com.greenfarm.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Comments")
public class Comment implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@NotNull(message = "Commentid không được để trống")
//	@Positive(message = "Commentid không được là số âm")
	private Integer commentid;
	
	@NotBlank(message = "Commenttext không được phép trống (chỉ chứa khoảng trắng)")
	private String commenttext;
	
	
	@NotNull(message = "Comment date không được phép trống")
//	@Past(message = "Comment date phải là ngày ở trong khóa khứ")
	private Date commentdate = new Date();
	
	
	@ManyToOne
	@JoinColumn(name = "userid")
	private User user;
	
	@ManyToOne	
	@JoinColumn(name = "TourID")
	Tour tour;
	
	
	@JsonIgnore
	@OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
	private List<ReComment> recomment;
//	@ManyToOne
//	@JoinColumn(name = "recomments")
//	private ReComment reComment;
	@Override
	public String toString() {
		return "";
	}
}
