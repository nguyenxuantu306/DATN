package com.greenfarm.restcontroller;

import java.util.List;
import java.util.function.Function;import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenfarm.dto.ContactDTO;
import com.greenfarm.entity.Booking;
import com.greenfarm.entity.Contact;
import com.greenfarm.service.ContactService;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/contact")
public class ContactRestController {
	@Autowired 
	ContactService contactService;
	
	@Autowired
	ModelMapper modelMapper;
	
	@GetMapping
	public ResponseEntity<List<ContactDTO>> getList(){
		
		List<Contact> contacts = contactService.FindAll();

	List<ContactDTO> contactDTOs = contacts.stream().map(contact -> modelMapper.map(contact, ContactDTO.class))
			.collect(Collectors.toList());

		 return new ResponseEntity<>(contactDTOs,HttpStatus.OK);		
 		
	}
	
	@GetMapping("{contactid}")
	public ResponseEntity<ContactDTO> getContact(@PathVariable("contactid") @Valid Integer contactid){
		Contact contact = contactService.findById(contactid);
		
		if (contact == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			ContactDTO contactDTO = modelMapper.map(contact, ContactDTO.class);
			return new ResponseEntity<>(contactDTO,HttpStatus.OK);
		}
		
	}
	
	@PutMapping("{contactid}")
	public ResponseEntity<ContactDTO> putContact(@PathVariable("contactid") @Valid Integer contactid,@RequestBody Contact contact){
		Contact updatecontact = contactService.Update(contact);
		
		if (contact == null) {
			return ResponseEntity.notFound().build();
		}else {
			ContactDTO contactDTO = modelMapper.map(contact, ContactDTO.class);
			return new ResponseEntity<>(contactDTO,HttpStatus.OK);
		}
		
	}
	
	@DeleteMapping("{contactid}")
	public ResponseEntity<Void> deletecontact(@PathVariable("contactid") @Valid Integer contactid){
		
		Contact existcontact = contactService.findById(contactid);
		if (existcontact == null) {
			return ResponseEntity.notFound().build();
		}else {
			
			contactService.deletebyid(contactid);
			return ResponseEntity.noContent().build();
		}
	}
	
}
