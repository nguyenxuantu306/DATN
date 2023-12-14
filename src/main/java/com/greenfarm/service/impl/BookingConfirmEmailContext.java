package com.greenfarm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.greenfarm.entity.AbstractEmailContext;
import com.greenfarm.entity.Booking;
import com.greenfarm.entity.User;
import com.greenfarm.service.BookingService;

public class BookingConfirmEmailContext extends AbstractEmailContext {
	private Booking booking;

	@Override
	public <T> void init(T context) {
		// we can do any common configuration setup here
		// like setting up some base URL and context
		Booking booking = (Booking) context;
		User customer = booking.getUser();
		put("firstName", customer.getFirstname());
		setTemplateLocation("emails/emailsendbookingqr");
		setSubject("ConFirm Booking");
		setFrom("no-reply@javadevjournal.com");
		setTo(customer.getEmail());

		put("bookingid", booking.getBookingid());
		put("Adultticketnumber", booking.getAdultticketnumber());
		put("bookingdate", booking.getBookingdate());
		put("Childticketnumber", booking.getChildticketnumber());
		put("bookingtour", booking.getTour().getTourname());
		put("eventdate", booking.getTourDateBooking().getTourdate().getTourdates());
		put("Totalprice", booking.getTotalprice());
	}

	public void setBooking(Booking booking) {
		this.booking = booking;

	}

	public void setQrCodeData(String data) {
		put("qrCodeData", data);
	}
//	public void buildVerificationUrl(final String baseURL, final String token) {
//		final String url = UriComponentsBuilder.fromHttpUrl(baseURL).path("/register/verify").queryParam("token", token)
//				.toUriString();
//		put("verificationURL", url);
//	}

}
