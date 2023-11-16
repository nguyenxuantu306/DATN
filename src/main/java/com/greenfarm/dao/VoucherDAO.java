package com.greenfarm.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.greenfarm.entity.Voucher;
import com.greenfarm.entity.VoucherOrder;

@EnableJpaRepositories
public interface VoucherDAO extends JpaRepository<Voucher, Integer>{

	Voucher findByVoucherid(long parseLong);
}
