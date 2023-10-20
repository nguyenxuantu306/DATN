package com.greenfarm.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.UserDAO;
import com.greenfarm.entity.User;
import com.greenfarm.service.UserService;

@Service
public class UserServerImpl implements UserService,UserDetailsService {

	@Autowired
	UserDAO dao;
	
	@Override
	public List<User> findAll() {
		return dao.findAll();
	}

	@Override
	public User findById(Integer userid) {
		return dao.findById(userid).get();
	}

	@Override
	public User create(User user) {
		return dao.save(user);
	}

	@Override
	public User update(User user) {
		return dao.save(user);
	}

	@Override
	public void delete(Integer userid) {
		dao.deleteById(userid);
	}

	// Security
	@Override
	public User findByEmail(String email) throws UsernameNotFoundException {
		Optional<User> userOptional = dao.findByEmail(email);
		if (userOptional.isPresent()) {
			return userOptional.get(); // Lấy đối tượng User ra khỏi Optional
		}
		return null;
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> userOptional = dao.findByEmail(email);
		if (userOptional.isPresent()) {
			User user = userOptional.get();

			try {
				
				List<GrantedAuthority> authorities = user.getUserRole().stream()
						.map(ur -> new SimpleGrantedAuthority(ur.getRole().getName())).collect(Collectors.toList());

				return org.springframework.security.core.userdetails.User.builder().username(email)
						.password(user.getPassword()).authorities(authorities).build();
			} catch (Exception e) {

				System.out.println("Lỗi xảy ra khi xử lý người dùng: " + e.getMessage());
			}
		} else {
			throw new UsernameNotFoundException("User not found with email: " + email);

		}
		return null;
	}
}
