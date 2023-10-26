package com.greenfarm.service;

import java.io.FileNotFoundException;

import com.greenfarm.entity.AbstractEmailContext;

import jakarta.mail.MessagingException;

public interface EmailService {

	public void sendSimpleEmail(String toAddress, String subject, String message);

	public void sendEmailWithAttachment(String toAddress, String subject, String message, String attachment)
			throws MessagingException, FileNotFoundException;

	public void sendMail(AbstractEmailContext email) throws MessagingException;

}
