package com.greenfarm.service.impl;

import java.util.Collection;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.Securetokendao;
import com.greenfarm.dao.UserDAO;
import com.greenfarm.dto.Provider;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.Role;
import com.greenfarm.entity.Securetoken;
import com.greenfarm.entity.User;
import com.greenfarm.entity.UserRole;
import com.greenfarm.exception.InvalidTokenException;
import com.greenfarm.exception.UnkownIdentifierException;
import com.greenfarm.exception.UserAlreadyExistException;
import com.greenfarm.service.EmailService;
import com.greenfarm.service.Securetokenservice;
import com.greenfarm.service.UserRoleService;
import com.greenfarm.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@Service
public class UserServerImpl implements UserService, UserDetailsService {

	@Autowired
	UserDAO dao;

	@Autowired
	UserRoleService userRoleService;

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
		return dao.findAllByIsdeletedFalse();
	}

	@Override
	public List<User> findAllDeletedUser() {
		return dao.findAllByIsdeletedTrue();
	}

	@Override
	public User findById(Integer userid) {
		return dao.findById(userid).get();
		// User user = dao.findById(userid).get();
		// if (user == null || !user.isAccountVerified()) {
		// // We will ignore in case the account is not verified or the account does not
		// exist
		// throw new UnkownIdentifierException("Unable to find the account or the
		// account is not active");
		// }
		//
		// return user;
	}

	//
	// System.out.println("ben trong impl");
	//// try {
	// Optional<User> account = dao.findByEmail(user.getEmail());
	//
	// System.out.println("sau find");
	// if (account.isPresent()) {
	// System.out.println("đã có tk");
	// throw new UserAlreadyExistException("đã có tài khoản dùng email này");
	// }
	// //emailExists(user.getEmail())
	// else{
	//
	//// User user1 = account.get();
	//// System.out.println(user1.getEmail());
	// try {
	//
	// System.out.println("ko to tk");
	// User userentity = new User();
	// BeanUtils.copyProperties(user, userentity);
	// userentity.setPassword(PE.encode(userentity.getPassword()));
	// System.out.println("luu us");
	// dao.save(userentity);
	// System.out.println("luu thanh cong");
	// sendRegistrationConfirmationEmail(userentity);
	// System.out.println("gui mail");
	// return userentity;
	// } catch (Exception e) {
	// e.getStackTrace();
	// }
	//
	//
	// }

	@Override
	public User update(User user) {
		return dao.save(user);
	}

	@Override
	public void delete(Integer userid) {
		dao.deleteByIsDeleted(userid);
	}

	@Override
	public List<User> getAdministrators() {
		return dao.getAdministrators();
	};

	// @Override
	// public UserDetails loadUserByUsername(String email) throws
	// UsernameNotFoundException {
	// Optional<User> userOptional = dao.findByEmail(email);
	// if(userOptional.isPresent()) {
	// User user = userOptional.get();
	//
	// try {
	// boolean enabled = !user.isAccountVerified();
	// List<GrantedAuthority> authorities = user.getUserRole().stream().map(ur ->
	// new
	// SimpleGrantedAuthority(ur.getRole().getName())).collect(Collectors.toList());
	// System.out.println(authorities);
	// return org.springframework.security.core.userdetails.User.builder()
	// .username(email).password(user.getPassword())
	// .disabled(enabled)
	// .authorities(authorities).build();
	// } catch (Exception e) {
	// System.out.println("Lỗi xảy ra khi xử lý người dùng: " + e.getMessage());
	// throw new UsernameNotFoundException("Không thể xử lý người dùng");
	// }
	// }else {
	// throw new UsernameNotFoundException("User not found with email: " + email);
	//
	// }
	//
	// }

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User account = dao.findByEmail(email).get();
		if (account == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		} else {
			boolean enabled = !account.isAccountVerified();
			return org.springframework.security.core.userdetails.User.builder().username(account.getEmail())
					.password(account.getPassword()).disabled(enabled).roles(account.getUserRole().stream()
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
		Optional<User> user = dao.findByEmail(email);
		if (user.isPresent()) {
			return user.get();
		} else {

			return null;
		}

	}

	@Override
	public void sendRegistrationConfirmationEmail(User user) {
		Securetoken secureToken = securetokenservice.createSecureToken();
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
		if (Objects.isNull(secureToken) || !com.mysql.cj.util.StringUtils.nullSafeEqual(token, secureToken.getToken())
				|| secureToken.isExpired()) {
			throw new InvalidTokenException("Token is not valid");
		}
		User user = dao.getOne(secureToken.getUser().getUserid());
		if (Objects.isNull(user)) {
			return false;
		}
		user.setAccountVerified(true);
		System.out.println("set true");
		dao.save(user); // let's same user details

		// we don't need invalid password now
		securetokenservice.removeToken(secureToken);
		return true;
	}

	@Override
	public boolean isPasswordMatching(String password, String confirmPassword) {

		return password.equals(confirmPassword);
	}

	@Override
	public boolean iscurrentPasswordMatching(User user, String password) {
		// Lấy mật khẩu hiện tại của người dùng
		String currentPassword = user.getPassword(); // Giả sử rằng mật khẩu đã được mã hóa

		// So sánh mật khẩu hiện tại với mật khẩu được cung cấp
		return PE.matches(password, currentPassword);
	}

	// @Override
	// public void forgottenPassword(String userName) throws
	// UnkownIdentifierException {
	// User user = dao.findByEmail(userName).get();
	// sendResetPasswordEmail(user);
	// }
	@Override
	public void forgottenPassword(String userName) throws UnkownIdentifierException {
		User user = dao.findByEmail(userName).orElseThrow(
				() -> new UnkownIdentifierException("Không tìm thấy người dùng với địa chỉ email đã nhập."));
		sendResetPasswordEmail(user);
	}

	@Override
	public void updatePassword(String password, String token) throws InvalidTokenException, UnkownIdentifierException {
		Securetoken securetoken = securetokenservice.findByToken(token);
		if (Objects.isNull(securetoken)
				|| !org.apache.commons.codec.binary.StringUtils.equals(token, securetoken.getToken())
				|| securetoken.isExpired()) {
			throw new InvalidTokenException("Token is not valid");
		}
		User user = dao.getOne(securetoken.getUser().getUserid());
		System.out.println(user.getEmail());
		if (Objects.isNull(user)) {
			throw new UnkownIdentifierException("unable to find user for the token");
		}
		securetokenservice.removeToken(securetoken);
		System.out.println("doi mk");
		user.setPassword(PE.encode(password));
		dao.save(user);

	}

	@Override
	public boolean loginDisabled(String username) {
		User user = dao.findByEmail(username).get();
		return false;
	}

	protected void sendResetPasswordEmail(User user) {
		Securetoken secureToken = securetokenservice.createSecureToken();
		secureToken.setUser(user);
		securetokendao.save(secureToken);
		ForgotPasswordEmailContext emailContext = new ForgotPasswordEmailContext();
		emailContext.init(user);
		emailContext.setToken(secureToken.getToken());
		emailContext.buildVerificationUrl("http://localhost:8080", secureToken.getToken());
		try {
			emailService.sendMail(emailContext);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loginFormOAuth2(OAuth2AuthenticationToken oauth2) {
		String email = oauth2.getPrincipal().getAttribute("email");
		String password = Long.toHexString(System.currentTimeMillis());

		UserDetails user = org.springframework.security.core.userdetails.User.withUsername(email)
				.password(PE.encode(password)).roles("2").build();
		Authentication auth = new UsernamePasswordAuthenticationToken(password, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);

	}

	@Override
	public UserDetails createNewUser(String email) throws UserAlreadyExistException {
		// Kiểm tra xem người dùng đã tồn tại trong hệ thống chưa

		if (emailExists(email)) {
			// Nếu người dùng đã tồn tại, trả về UserDetails của họ
			throw new UserAlreadyExistException("đã có tài khoản dùng email này");
		} else {
			// Nếu người dùng chưa tồn tại, tạo một tài khoản mới
			User newUser = new User();
			newUser.setEmail(email);
			// Thêm quyền (roles) tùy ý cho tài khoản mới
			// newUser.setRoles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
			// .stream()
			// .map(er -> er.getRole().getName()).collect(Collectors.toList()).toArray(new
			// String[0]))
			List<UserRole> lusrl = userRoleService.findAll();
			newUser.setUserRole(lusrl);
			// Lưu tài khoản mới vào cơ sở dữ liệu
			newUser.setFirstname("google");
			newUser.setLastname("google");
			newUser.setPhonenumber("1234567");
			dao.save(newUser);

			// Trả về UserDetails của tài khoản mới
			return null;

		}

	}

	@Override
	public void processOAuthPostLogin(String username) {
		System.out.println("start check account");
		Optional<User> existUser = dao.findByEmail(username);
		System.out.println("it exxit");
		if (existUser.isEmpty()) {
			User newUser = new User();
			// newUser.setFirstname("google");
			// newUser.setLastname("google");
			// newUser.setPhonenumber("0000000000");
			newUser.setEmail(username);
			newUser.setProvider(Provider.GOOGLE);
			newUser.setAccountVerified(true);
			newUser.setCreateddate(new Date());
			System.out.println("cretea a account");
			dao.save(newUser);
			System.out.println("done");
		}

	}

	@Override
	public List<Report> getTotalPurchaseByUser() {
		return dao.totalPurchaseByUser();
	}

	@Override
	public List<Report> getBookingTotalPurchaseByUser() {
		return dao.BookingTotalPurchaseByUser();
	}

	@Override
	public User save(User user) {
		return dao.save(user);

	}

	@Override
	public User create(User user) throws UserAlreadyExistException {
		// Kiểm tra xem user đã tồn tại chưa
		if (emailExists(user.getEmail())) {
			// Nếu đã tồn tại, ném ngoại lệ

			System.out.println("da co tk");
			throw new UserAlreadyExistException("Đã có tài khoản sử dụng email này");
		} else {
			// Nếu chưa tồn tại, tạo user và trả về
			User userEntity = new User();
			BeanUtils.copyProperties(user, userEntity);
			userEntity.setPassword(PE.encode(userEntity.getPassword()));
			System.out.println(userEntity.getEmail());
			System.out.println(userEntity.getFirstname());
			System.out.println(userEntity.getLastname());
			System.out.println(userEntity.getPassword());
			System.out.println(userEntity.getPhonenumber());

			userEntity.setBirthday(null);
			userEntity.setAddress(null);
			userEntity.setGender(null);
			userEntity.setImage(null);
			System.out.println("Lưu user vào database");
			try {
				dao.save(userEntity);
			} catch (Exception e) {
				System.out.println("ko luu dc");
				e.printStackTrace();
			}

			sendRegistrationConfirmationEmail(userEntity);
			System.out.println("Gửi email xác nhận đăng ký");
			return userEntity;
		}
	}

	@Override
	public boolean emailExists(String email) {
		Optional<User> account = dao.findByEmail(email);
		return account.isPresent();
	}

	@Override
	public User createADMIN(@Valid User user) throws UserAlreadyExistException {
		// Kiểm tra xem user đã tồn tại chưa
		if (emailExists(user.getEmail())) {
			// Nếu đã tồn tại, ném ngoại lệ
			System.out.println("da co tk");
			throw new UserAlreadyExistException("Đã có tài khoản sử dụng email này");
		} else {
			// Nếu chưa tồn tại, tạo user và trả về
			User userEntity = new User();
			BeanUtils.copyProperties(user, userEntity);
			userEntity.setPassword(PE.encode(userEntity.getPassword()));
			userEntity.setAccountVerified(true);
			System.out.println("Lưu user vào database");
			try {
				dao.save(userEntity);
			} catch (Exception e) {
				System.out.println("ko luu dc");
				e.printStackTrace();
			}

			sendRegistrationConfirmationEmail(userEntity);
			System.out.println("Gửi email xác nhận đăng ký");
			return userEntity;
		}
	}

	@Override
	public List<User> findByKeyword(String keyword) {
		// Triển khai tìm kiếm theo từ khóa trong repository
		return dao.findByKeyword(keyword);
	}

}
