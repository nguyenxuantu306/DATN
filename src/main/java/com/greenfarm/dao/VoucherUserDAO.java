package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.greenfarm.entity.User;
import com.greenfarm.entity.Voucher;
import com.greenfarm.entity.VoucherUser;

public interface VoucherUserDAO extends JpaRepository<VoucherUser, Integer>{

//	@Query("SELECT o FROM VoucherUser o WHERE o.user.email =?1")
//	Object findByEfindByIdAccountmail(String email);
	List<VoucherUser> findByUser(User user);
	
	
	@Query("SELECT vu FROM VoucherUser vu WHERE LOWER(vu.user.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(vu.voucher.code) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<VoucherUser> findByKeyword(@Param("keyword") String keyword);

	boolean existsByUserUseridAndVoucherVoucherid(Integer userId, Integer voucherId);

}
