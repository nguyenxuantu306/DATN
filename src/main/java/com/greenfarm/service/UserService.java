package com.greenfarm.service;

import java.util.List;
import com.greenfarm.entity.User;
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
	
	boolean  emailExists(String email);
	
	//yeyye
	
	boolean checkIfUserExist(final String email);
    void sendRegistrationConfirmationEmail(final User user);
    boolean verifyUser(final String token) throws com.greenfarm.exception.InvalidTokenException;
    public boolean iscurrentPasswordMatching(User user,String password) ;

    public boolean isPasswordMatching(String password, String confirmPassword);

}
