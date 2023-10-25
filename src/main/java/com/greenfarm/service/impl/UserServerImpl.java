package com.greenfarm.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.Securetokendao;
import com.greenfarm.dao.UserDAO;
import com.greenfarm.entity.Role;
import com.greenfarm.entity.Securetoken;
import com.greenfarm.entity.User;
import com.greenfarm.exception.InvalidTokenException;
import com.greenfarm.exception.UserAlreadyExistException;
import com.greenfarm.service.EmailService;
import com.greenfarm.service.Securetokenservice;
import com.greenfarm.service.UserService;

@Service
public class UserServerImpl implements UserService, UserDetailsService {

	@Autowired
	UserDAO dao;
	
	@Autowired
	Securetokenservice securetokenservice;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	Securetokendao securetokendao;
	
	@Autowired
	PasswordEncoder PE;
	
	@Override
	public List<User> findAll() {
		return dao.findAll();
	}

	@Override
	public User findById(Integer userid) {
		return dao.findById(userid).get();
//		User user = dao.findById(userid).get();
//		if (user == null || !user.isAccountVerified()) {
//	        // We will ignore in case the account is not verified or the account does not exist
//	        throw new UnkownIdentifierException("Unable to find the account or the account is not active");
//	    }
//		
//		return user;
	}

	@Override
	public User create(User user) throws UserAlreadyExistException {
		if (!emailExists(user.getEmail())) {
			throw new  UserAlreadyExistException("đã có tài khoản dùng email này");
		}
		User userentity = new User();
		BeanUtils.copyProperties(user, userentity);
		userentity.setPassword(PE.encode(userentity.getPassword()));
		dao.save(userentity);
		sendRegistrationConfirmationEmail(userentity);

		return userentity;
	}

	@Override
	public User update(User user) {
		return dao.save(user);
	}

	@Override
	public void delete(Integer userid) {
		dao.deleteById(userid);
	}
	
	@Override
	public List<User> getAdministrators() {
		// TODO Auto-generated method stub
		return dao.getAdministrators();
	};

//	@Override
//	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//		// TODO Auto-generated method stub
//		Optional<User> userOptional = dao.findByEmail(email);
//		if(userOptional.isPresent()) {
//			User user = userOptional.get();
//			
//			try {
//				boolean enabled = !user.isAccountVerified();
//				List<GrantedAuthority> authorities = user.getUserRole().stream().map(ur -> new SimpleGrantedAuthority(ur.getRole().getName())).collect(Collectors.toList());
//				System.out.println(authorities);
//				return org.springframework.security.core.userdetails.User.builder()
//							.username(email).password(user.getPassword())
//							.disabled(enabled)
//							.authorities(authorities).build();
//			} catch (Exception e) {
//				// TODO: handle exception
//				 System.out.println("Lỗi xảy ra khi xử lý người dùng: " + e.getMessage());
//		            throw new UsernameNotFoundException("Không thể xử lý người dùng");
//		       }
//		}else {
//			 throw new UsernameNotFoundException("User not found with email: " + email);
//					
//		}
//			
//	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User account = dao.findByEmail(email).get();
		if (account == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		} else {
			boolean enabled = !account.isAccountVerified();
			return org.springframework.security.core.userdetails.User.builder().username(account.getEmail())
					.password(account.getPassword())
					.disabled(enabled)
					.roles(account.getUserRole().stream()
							.map(er -> er.getRole().getName()).collect(Collectors.toList()).toArray(new String[0]))
					.build();
		}
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		Collection<? extends GrantedAuthority> mapRoles = roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
		return mapRoles;
	}

	@Override
	public User findByEmail(String email) {	
		User user = dao.findByEmail(email).get();
		return user;
	}

	@Override
	public boolean emailExists(String email) {
		return dao.findByEmail(email) != null;
	}

	@Override
	public boolean checkIfUserExist(String email) {
		return false;
	}

	@Override
	public void sendRegistrationConfirmationEmail(User user) {
		Securetoken secureToken= securetokenservice.createSecureToken();
        secureToken.setUser(user);
        securetokendao.save(secureToken);
        AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
        emailContext.init(user);
        emailContext.setToken(secureToken.getToken());
        emailContext.buildVerificationUrl("http://localhost:8080", secureToken.getToken());
        try {
            emailService.sendMail(emailContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	@Override
	public boolean verifyUser(String token) throws InvalidTokenException {
		Securetoken secureToken = securetokenservice.findByToken(token);
		if (Objects.isNull(secureToken) || !com.mysql.cj.util.StringUtils.nullSafeEqual(token, secureToken.getToken()) || secureToken.isExpired()  ) {
			 throw new InvalidTokenException("Token is not valid");
		}
        User user = dao.getOne(secureToken.getUser().getUserid());
        if(Objects.isNull(user)){
            return false;
        }
        user.setAccountVerified(true);
        System.out.println("set true");
        dao.save(user); // let's same user details

        // we don't need invalid password now
        securetokenservice.removeToken(secureToken);
        return true;
	}

}
