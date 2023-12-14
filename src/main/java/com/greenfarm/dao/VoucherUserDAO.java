package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.User;
import com.greenfarm.entity.VoucherUser;

public interface VoucherUserDAO extends JpaRepository<VoucherUser, Integer> {

//	@Query("SELECT o FROM VoucherUser o WHERE o.user.email =?1")
//	Object findByEfindByIdAccountmail(String email);
	List<VoucherUser> findByUser(User user);
}
