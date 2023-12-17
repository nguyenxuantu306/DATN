package com.greenfarm.service;

import java.util.List;

import com.greenfarm.entity.Contact;

public interface ContactService {
	List<Contact> FindAll();
	List<Contact> finContactsByDate();
	Contact Create(Contact contact);
	Contact Update(Contact contact);
	void deletebyid(Integer contactid);
	Contact findById(Integer contactid);
}
