package com.greenfarm;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.greenfarm.exception.Globallnterceptor;



@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	@Autowired
	Globallnterceptor globallntercreptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(globallntercreptor).addPathPatterns("/**").excludePathPatterns("/rest/**", "/admin/**",
				"/assets/**");
	}
}
