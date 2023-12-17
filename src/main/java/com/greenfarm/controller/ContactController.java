package com.greenfarm.controller;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.greenfarm.dto.ContactDTO;
import com.greenfarm.entity.Contact;
import com.greenfarm.service.ContactService;
import com.greenfarm.utils.Log;

import jakarta.validation.Valid;

@Controller
public class ContactController {
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	ContactService contactService;
	
	@GetMapping("/contact")
	public String Contact(Model model) {
		model.addAttribute("contact",new ContactDTO());
		return "contact";
	}
	
	@PostMapping("/contact")
	public String saveContact(Model model, @ModelAttribute("contact") @Valid ContactDTO contactinfo,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			// Nếu có lỗi từ dữ liệu người dùng, không cần kiểm tra tiếp và xử lý lỗi.
			model.addAttribute("error", "Thông tin không hợp lệ. Vui lòng kiểm tra lại.");
			return "contact";
		}else {
			//Booking booking = modelMapper.map(bookingDto, Booking.class);
			com.greenfarm.entity.Contact contact = modelMapper.map(contactinfo, Contact.class);
			System.out.println(contact.getEmail());
			try {
				contact.setCreateddate(new Date());
				contactService.Create(contact);
				Log.info("Lưu liên hệ và phản hồi của khách hàng"+contact.getEmail());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Log.error("lỗi không gửi được liên hệ");
			}
			
			return "contact";
		}
		
		
		
	}
	
	
}
