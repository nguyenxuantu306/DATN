package com.greenfarm.service;

import java.io.FileNotFoundException;

import com.greenfarm.entity.AbstractEmailContext;
import com.greenfarm.service.impl.BookingConfirmEmailContext;
import com.greenfarm.service.impl.OrderEmailContext;

import jakarta.mail.MessagingException;

public interface EmailService {

	public void sendSimpleEmail(String toAddress, String subject, String message);

	public void sendEmailWithAttachment(String toAddress, String subject, String message, String attachment)
			throws MessagingException, FileNotFoundException;

	public void sendMail(AbstractEmailContext email) throws MessagingException;

	public void sendEmailWithBooking(String toAddress, String subject, String message, String attachment)
			throws MessagingException, FileNotFoundException;
	
	public void sendMail(OrderEmailContext email) throws MessagingException;
	
	public void sendBookingMail(BookingConfirmEmailContext email) throws MessagingException;

}
