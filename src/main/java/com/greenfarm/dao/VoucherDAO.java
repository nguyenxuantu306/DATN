package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import com.greenfarm.entity.Category;
import com.greenfarm.entity.Voucher;

@EnableJpaRepositories
public interface VoucherDAO extends JpaRepository<Voucher, Integer>{

	Voucher findByVoucherid(long parseLong);
	
	
	// tìm kiếm keywword
	@Query("SELECT c FROM Voucher c WHERE LOWER(c.code) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<Voucher> findByKeyword(@Param("keyword") String keyword);
}
