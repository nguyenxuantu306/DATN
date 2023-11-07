package com.greenfarm.service;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.greenfarm.entity.User;
import com.greenfarm.exception.InvalidTokenException;
import com.greenfarm.exception.UnkownIdentifierException;
import com.greenfarm.exception.UserAlreadyExistException;

public interface UserService {

	List<User> findAll();

	User findById(Integer userid) throws UnkownIdentifierException;

	User create(User user) throws UserAlreadyExistException;

	User update(User user);

	void delete(Integer userid);

	// Security
	User findByEmail(String email);

	public List<User> getAdministrators();

	boolean emailExists(String email);

	// yeyye

	boolean checkIfUserExist(final String email);

	

    void sendRegistrationConfirmationEmail(final User user);
    boolean verifyUser(final String token) throws com.greenfarm.exception.InvalidTokenException;
    public boolean iscurrentPasswordMatching(User user,String password) ;

    public boolean isPasswordMatching(String password, String confirmPassword);
    
    void forgottenPassword(final String userName) throws UnkownIdentifierException;
    void updatePassword(final String password, final String token) throws InvalidTokenException, UnkownIdentifierException;
    boolean loginDisabled(final String username);
    
    public UserDetails createNewUser(String email) throws UserAlreadyExistException;
    public void loginFormOAuth2(OAuth2AuthenticationToken oauth2);
    public void processOAuthPostLogin(String username);
}
