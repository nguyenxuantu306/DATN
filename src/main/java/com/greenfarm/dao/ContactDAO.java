package com.greenfarm.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenfarm.entity.Contact;

public interface ContactDAO extends JpaRepository<Contact, Integer>{
	List<Contact> findBycreateddate(Date createdate);
	
	
	

}
