package com.greenfarm.dao;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.greenfarm.entity.Address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AddressDAO extends JpaRepository<Address, Integer> {

	@Query("SELECT o FROM Address o WHERE o.user.email =?1")
	List<Address> findByEfindByIdAccountmail(String email);

	@Modifying
	@Query("UPDATE Address a SET a.Active = true WHERE a.AddressID = :addressId")
	void setActiveStatus(@Param("addressId") Integer addressId);

}
