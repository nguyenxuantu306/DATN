package com.greenfarm.service.impl;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.greenfarm.entity.AbstractEmailContext;
import com.greenfarm.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class Emailserviceimpl implements EmailService {

	@Autowired
	public JavaMailSender emailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Override
	public void sendSimpleEmail(String toAddress, String subject, String message) {
		SimpleMailMessage simplemailmessage = new SimpleMailMessage();
		simplemailmessage.setTo(toAddress);
		simplemailmessage.setSubject(subject);
		simplemailmessage.setText(message);
		emailSender.send(simplemailmessage);
	}

	@Override
	public void sendEmailWithAttachment(String toAddress, String subject, String message, String attachment)
			throws MessagingException, FileNotFoundException {
		MimeMessage mimeMessage = emailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
		messageHelper.setTo(toAddress);
		messageHelper.setSubject(subject);
		messageHelper.setText(message);
		FileSystemResource file = new FileSystemResource(ResourceUtils.getFile(attachment));
		messageHelper.addAttachment("Purchase Order", file);
		emailSender.send(mimeMessage);
	}

	@Override
	public void sendMail(AbstractEmailContext email) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,
				MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
		org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
		context.setVariables(email.getContext());
		String emailContent = templateEngine.process(email.getTemplateLocation(), context);

		mimeMessageHelper.setTo(email.getTo());
		mimeMessageHelper.setSubject(email.getSubject());
		mimeMessageHelper.setFrom(email.getFrom());
		mimeMessageHelper.setText(emailContent, true);
		emailSender.send(message);
	}

}
