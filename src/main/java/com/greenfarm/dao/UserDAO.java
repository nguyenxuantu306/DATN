package com.greenfarm.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.greenfarm.entity.Report;
import com.greenfarm.entity.User;

public interface UserDAO extends JpaRepository<User, Integer> {

	@Query("SELECT DISTINCT ur.user FROM UserRole ur WHERE ur.role.id IN (1,2)")
	List<User> getAdministrators();

	Optional<User> findByEmail(String email);

	@Query("SELECT NEW Report(u, SUM(CASE WHEN so.name = 'Giao hàng thành công' THEN od.totalprice ELSE 0 END), COUNT(o)) "
			+ "FROM User u " + "LEFT JOIN u.order o " + "LEFT JOIN o.orderDetail od " + "LEFT JOIN o.statusOrder so "
			+ "GROUP BY u " + "ORDER BY SUM(CASE WHEN so.name = 'Giao hàng thành công' THEN od.totalprice ELSE 0 END)")
	List<Report> totalPurchaseByUser();

	@Query("SELECT NEW Report(u, SUM(CASE WHEN sb.name = 'Đặt tour thành công' THEN b.Totalprice ELSE 0 END), COUNT(b)) "
			+ "FROM User u " + "LEFT JOIN u.booking b " + "LEFT JOIN b.statusbooking sb " + "GROUP BY u "
			+ "ORDER BY SUM(CASE WHEN sb.name = 'Giao hàng thành công' THEN b.Totalprice ELSE 0 END)")
	List<Report> BookingTotalPurchaseByUser();

	@Query("SELECT p FROM User p WHERE p.isdeleted = true")
	List<User> findAllDeletedUser();

	@Query("SELECT p FROM User p WHERE p.userid = :id")
	User findUserById(@Param("id") Integer id);

	default void deleteByIsDeleted(Integer id) {
		User user = findUserById(id);
		if (user != null) {
			user.setIsDeleted(true);
			save(user);
		}
	}

	List<User> findAllByIsdeletedFalse();

	List<User> findAllByIsdeletedTrue();
}
