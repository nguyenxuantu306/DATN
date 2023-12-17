package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.ContactDAO;
import com.greenfarm.entity.Contact;
import com.greenfarm.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService{

	@Autowired
	ContactDAO contactDAO;
		
	@Override
	public List<Contact> FindAll() {
		// TODO Auto-generated method stub
		return contactDAO.findAll();
	}

	@Override
	public List<Contact> finContactsByDate() {
		// TODO Auto-generated method stub
		return contactDAO.findBycreateddate(null);
	}

	@Override
	public Contact Create(Contact contact) {
		// TODO Auto-generated method stub
		return contactDAO.save(contact);
	}

	@Override
	public Contact Update(Contact contact) {
		// TODO Auto-generated method stub
		return contactDAO.save(contact);
	}

	@Override
	public void deletebyid(Integer contactid) {
		// TODO Auto-generated method stub
		contactDAO.deleteById(contactid);
	}

	@Override
	public Contact findById(Integer contactid) {
		// TODO Auto-generated method stub
		return contactDAO.findById(contactid).get();
	}

}
