package com.greenfarm.service.impl;

import org.springframework.web.util.UriComponentsBuilder;

import com.greenfarm.entity.AbstractEmailContext;
import com.greenfarm.entity.User;


public class AccountVerificationEmailContext extends AbstractEmailContext{
	private String token;


    @Override
    public <T> void init(T context){
        //we can do any common configuration setup here
        // like setting up some base URL and context
        User customer = (User)  context; // we pass the customer informati
        put("firstName", customer.getFirstname());
        setTemplateLocation("emails/email-verification");
        //setTemplateLocation("Email");
        setSubject("Complete your registration");
        setFrom("no-reply@javadevjournal.com");
        setTo(customer.getEmail());
    }

    public void setToken(String token) {
        this.token = token;
        put("token", token);
    }

    public void buildVerificationUrl(final String baseURL, final String token){
        final String url= UriComponentsBuilder.fromHttpUrl(baseURL)
                .path("/register/verify").queryParam("token", token).toUriString();
        put("verificationURL", url);
    }
	   
}
