package com.greenfarm.ServiceIplm;

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

import com.greenfarm.DAO.UserDAO;
import com.greenfarm.ENTITY.User;
import com.greenfarm.Service.UserService;

@Service
public class UserServerImpl implements UserService, UserDetailsService {

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

	@Override
	public User findByEmail(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<User> userOptional = dao.findByEmail(email);
		if (userOptional.isPresent()) {
			return userOptional.get(); // Lấy đối tượng User ra khỏi Optional
		}
		return null;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<User> userOptional = dao.findByEmail(email);
		if (userOptional.isPresent()) {
			User user = userOptional.get();

			try {
				
				List<GrantedAuthority> authorities = user.getUserRole().stream()
						.map(ur -> new SimpleGrantedAuthority(ur.getRole().getName())).collect(Collectors.toList());

				return org.springframework.security.core.userdetails.User.builder().username(email)
						.password(user.getPassword()).authorities(authorities).build();
			} catch (Exception e) {
				// TODO: handle exception

				System.out.println("Lỗi xảy ra khi xử lý người dùng: " + e.getMessage());
			}
		} else {
			throw new UsernameNotFoundException("User not found with email: " + email);

		}
		return null;
	}

}
