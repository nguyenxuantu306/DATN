package com.greenfarm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.google.api.client.util.Value;
import com.greenfarm.service.UserService;
import com.greenfarm.service.impl.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class Authconfig {
	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	UserService userService;


	@Autowired
	private CustomAuthSucessHandler authSucessHandler;
	
	@Autowired
	CustomLoginOAuth2successHandler customLoginOAuth2successHandler;

	
	@Value("${spring.security.oauth2.client.registration.client-id}")
	private String clientId;

	

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}

	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
//		auth.userDetailsService(userDetailsService)
//		.passwordEncoder(passwordEncoder());
	}
	//warnign
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.authenticationProvider(authenticationProvider());
//	}
	
	@Autowired
	private CustomOAuth2UserService oauthUserService;
	
	
	
	@SuppressWarnings("deprecation")
	@Bean
	public SecurityFilterChain fillterchain(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().disable();

		http.authorizeRequests(authorize -> authorize
				.requestMatchers("/profile", "/booking/*","/checkout", "/cart").authenticated()
				.requestMatchers("/assetsAdmin/", "/admin")
				.hasRole("Administrator")
				.requestMatchers("/assets/*").permitAll()
				.anyRequest().permitAll())
		 ;

		http.formLogin(form -> form.loginPage("/login")
				.successHandler(authSucessHandler)
				
		/* .loginProcessingUrl("/") */

		)
		
		
		.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logoff")).permitAll())
		.oauth2Login(oauth2Login ->
        oauth2Login
        	.loginPage("/auth/login/form")
        	.successHandler(customLoginOAuth2successHandler)
        	//.defaultSuccessUrl("/oauth2/login/success",true)
        	.failureUrl("/auth/login/error")
        	.authorizationEndpoint().baseUri("/oauth2/authorization")
//            .userInfoEndpoint()
//                    .userService(oauthUserService)
//                    .and()
//                    .successHandler(auth2successHandler)
                  //  .failureHandler((request, response, exception) -> );
                    
            
    );
		
		return http.build();
	}
	

	
	@Bean
	public OAuth2AuthorizedClientService authorizedClientService(
	        ClientRegistrationRepository clientRegistrationRepository) {
	    return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
	}

	
	
	
}
