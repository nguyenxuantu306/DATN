package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import com.greenfarm.entity.Category;
import com.greenfarm.entity.Voucher;

@EnableJpaRepositories
public interface VoucherDAO extends JpaRepository<Voucher, Integer> {

	Voucher findByVoucherid(long parseLong);
	
	
	// tìm kiếm keywword
	@Query("SELECT c FROM Voucher c WHERE LOWER(c.code) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<Voucher> findByKeyword(@Param("keyword") String keyword);
	
	@Query("SELECT v FROM Voucher v WHERE v.isdeleted = true")
	Voucher findAllDeletedVouchers(@Param("id") Integer id);

	@Query("SELECT v FROM Voucher v WHERE v.voucherid = :id")
	Voucher findVoucherById(@Param("id") Integer id);
	
	default void deleteByIsDeleted(Integer id) {
		Voucher voucher = findVoucherById(id);
		if (voucher != null) {
			voucher.setIsDeleted(true);
			save(voucher);
		}
	}

	List<Voucher> findAllByIsdeletedFalse();

	List<Voucher> findAllByIsdeletedTrue();

	Page<Voucher> findAllByIsdeletedFalse(Pageable pageable);
}
