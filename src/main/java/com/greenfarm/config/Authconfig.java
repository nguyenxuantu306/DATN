package com.greenfarm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import java.beans.Customizer;
import java.io.IOException;
import java.security.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.google.api.client.util.Value;
import com.greenfarm.dto.Provider;
import com.greenfarm.entity.CustomOAuth2User;
import com.greenfarm.entity.User;
import com.greenfarm.service.UserService;

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
	CustomOAuth2successHandler auth2successHandler;

	
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
				.hasRole("Administrator").anyRequest().permitAll());

		http.formLogin(form -> form.loginPage("/login")
				.successHandler(authSucessHandler)
				
		/* .loginProcessingUrl("/") */

		)
		
		
		.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logoff")).permitAll())
		.oauth2Login(oauth2Login ->
        oauth2Login
        	.loginPage("/login")
            .userInfoEndpoint()
                    .userService(oauthUserService)
                    .and()
                    .successHandler(auth2successHandler)
                  //  .failureHandler((request, response, exception) -> );
                    
            
    );
//		.oauth2Login()
//		.loginPage("/login")
//		
//		.userInfoEndpoint()
//		.userService(oauthUserService)
//		.and()
//		.successHandler(
//				new AuthenticationSuccessHandler() {
//					
//					@Override
//					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//							Authentication authentication) throws IOException, ServletException {
//						// TODO Auto-generated method stub
//					
//						    // Nếu bạn muốn lấy ra đối tượng User (nếu đã chuyển đổi)
//						    Object principal = authentication.getPrincipal();
//						    System.out.println("lấy auth");
//						    System.out.println("thogn tin"+principal.toString());
//						    System.out.println(principal instanceof CustomOAuth2User?true:false);
//						    if (principal instanceof CustomOAuth2User) {
//						    	System.out.println("trong if");
//						       // CustomOAuth2User customOAuth2User = (CustomOAuth2User) principal;
//						    	CustomOAuth2User oauth2User = (CustomOAuth2User) principal;
//						    	String email = oauth2User.getAttribute("email");
//						    	System.out.println(email);
//						    	userService.processOAuthPostLogin(email);
//						    	System.out.println("laod");
//						    	
//						    	System.out.println("load ok");
//						    	response.sendRedirect("/");
//						    //	String userEmail = customOAuth2User.getEmail();
//						      // System.out.println("us email:"+userEmail);
//						        
//						        // Thực hiện các xử lý khác với đối tượng CustomOAuth2User
//						    }
//						}
//						
//					}
//				
//)
//		
//		;
		
		return http.build();
	}
	

//	@Autowired
//	private DefaultOAuth2UserService oauthUserService;
	
//	@Configuration
//	public class OAuth2LoginConfig {
//
//		@Bean
//		public ClientRegistrationRepository clientRegistrationRepository() {
//			return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
//		}

//		private ClientRegistration googleClientRegistration() {
//			return ClientRegistration.withRegistrationId("google")
//				.clientId("google-client-id")
//				.clientSecret("google-client-secret")
//				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//				.redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
//				.scope("openid", "profile", "email", "address", "phone")
//				.authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
//				.tokenUri("https://www.googleapis.com/oauth2/v4/token")
//				.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
//				.userNameAttributeName(IdTokenClaimNames.SUB)
//				.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
//				.clientName("Google")
//				.build();
//		}
//	}
//	


//	@Bean
//	public OAuth2AuthorizedClientService authorizedClientService(
//			ClientRegistrationRepository clientRegistrationRepository) {
//		return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
//	}
	
	@Bean
	public OAuth2AuthorizedClientService authorizedClientService(
	        ClientRegistrationRepository clientRegistrationRepository) {
	    return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
	}

	
	
	
}
