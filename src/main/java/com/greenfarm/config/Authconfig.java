package com.greenfarm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.google.api.client.util.Value;

@Configuration
@EnableWebSecurity
public class Authconfig {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private CustomAuthSucessHandler authSucessHandler;

	@Autowired
	private CustomFailHandle customFailHandle;

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
<<<<<<< HEAD
        http.csrf(csrf -> csrf.disable()).cors(cors -> cors.disable());
=======
		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.disable());
>>>>>>> mergethu

		http.authorizeRequests(authorize -> authorize
				.requestMatchers("/profile", "/booking/*", "/checkout", "/cart").authenticated()
				.requestMatchers("/assetsAdmin/", "/admin")
				.hasRole("Administrator").anyRequest().permitAll());

		http.formLogin(form -> form.loginPage("/login")
				.successHandler(authSucessHandler)
				.failureHandler(customFailHandle)
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
