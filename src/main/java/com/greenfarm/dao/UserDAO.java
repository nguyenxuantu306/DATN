package com.greenfarm.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenfarm.entity.Report;
import com.greenfarm.entity.User;

public interface UserDAO extends JpaRepository<User, Integer> {

	@Query("SELECT DISTINCT ur.user FROM UserRole ur WHERE ur.role.id IN (1,2)")
	List<User> getAdministrators();

	Optional<User> findByEmail(String email);

	
	@Query("SELECT NEW Report(u, SUM(CASE WHEN so.name = 'Giao hàng thành công' THEN od.totalprice ELSE 0 END), COUNT(o)) " +
		       "FROM User u " +
		       "LEFT JOIN u.order o " +
		       "LEFT JOIN o.orderDetail od " +
		       "LEFT JOIN o.statusOrder so " +
		       "GROUP BY u " +
		       "ORDER BY SUM(CASE WHEN so.name = 'Giao hàng thành công' THEN od.totalprice ELSE 0 END)")
		List<Report> totalPurchaseByUser();


//	@Query("SELECT new Report(o, sum(o.totalprice * o.quantityordered), sum(o.quantityordered)) FROM OrderDetail o"
//			+ " GROUP BY o" + " ORDER BY sum(o.totalprice *o.quantityordered) DESC")
}
