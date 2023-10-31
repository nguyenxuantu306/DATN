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

    @Value("${spring.security.oauth2.client.registration.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.provider-uri}")
    private String providerUri;
	
	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@SuppressWarnings("deprecation")
	@Bean
	public SecurityFilterChain fillterchain(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().disable();

		http.authorizeRequests(authorize -> authorize
				.requestMatchers("/profile").authenticated()
				
				.requestMatchers("/assetsAdmin/**", "/admin").hasRole("Administrator")
				.anyRequest().permitAll()
				
				);
		http.rememberMe().rememberMeParameter("remember");
		http.formLogin(form -> form
				.loginPage("/login")
				.successForwardUrl("/")
				 .successHandler(successHandler())
		)
		.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logoff"))
				.addLogoutHandler(customLogoutHandler)
				.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
				.permitAll());
//		http.oauth2Login().tokenEndpoint();
		
		
//		/*
//		 * http.oauth2Login() .loginPage("/oauth2/login") .userInfoEndpoint()
//		 * .userService(oauthUserService) .and().successHandler(new
//		 * AuthenticationSuccessHandler() {
//		 * 
//		 * @Override public void onAuthenticationSuccess(HttpServletRequest request,
//		 * HttpServletResponse response, Authentication authentication) throws
//		 * IOException, ServletException { // TODO Auto-generated method stub
//		 * System.out.println("AuthenticationSuccessHandler invoked");
//		 * System.out.println("Authentication name: " + authentication.getName());
//		 * CustomOAuth2User oauthUser = (CustomOAuth2User)
//		 * authentication.getPrincipal();
//		 * 
//		 * userService.processOAuthPostLogin(oauthUser.getEmail());
//		 * //userService.processOAuthPostLogin(oauthUser.getEmail());
//		 * 
//		 * response.sendRedirect("/");
//		 * 
//		 * } }) ;
//		 */
		
//   		.defaultSuccessUrl("/oauth2/login/success",true)
//   		.failureUrl("/oauth2/login/error")
//   		.authorizationEndpoint().baseUri("/oauth2/authorization")  ;

		return http.build();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Autowired
    private CustomOAuth2UserService oauthUserService;

    @Bean
    public CustomAuthSucessHandler successHandler(){
        return  new CustomAuthSucessHandler();
    }
}
