package com.greenfarm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.greenfarm.entity.Voucher;

public interface VoucherDAO extends JpaRepository<Voucher, Integer>{

}
