package com.greenfarm.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.google.api.client.util.Value;
import com.greenfarm.entity.CustomOAuth2User;
import com.greenfarm.service.UserService;
import com.greenfarm.service.impl.CustomOAuth2UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class Authconfig {
	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	UserService userService;

	@Autowired
	private CustomLogoutHandler customLogoutHandler;

	@Value("${spring.security.oauth2.client.registration.client-id}")
	private String clientId;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@SuppressWarnings("deprecation")
	@Bean
	public SecurityFilterChain fillterchain(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().disable();

		http.authorizeRequests(authorize -> authorize
				.requestMatchers("/profile", "/booking/*","/checkout", "/cart").authenticated()
				.requestMatchers("/assetsAdmin/", "/admin")
				.hasRole("Administrator").anyRequest().permitAll());

		http.formLogin(form -> form.loginPage("/login")
		/* .loginProcessingUrl("/") */

		).logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logoff")).permitAll());
		// .oauth2Login()
		// .loginPage("/oauth2/login/form")
		// .defaultSuccessUrl("/oauth2/login/success",true)
		// .failureUrl("/oauth2/login/error")
		// .authorizationEndpoint().baseUri("/oauth2/authorization") ;

		return http.build();
	}
	
}
