package com.greenfarm.restcontroller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenfarm.dto.UserDTO;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.User;
import com.greenfarm.exception.UnkownIdentifierException;
import com.greenfarm.exception.UserAlreadyExistException;
import com.greenfarm.service.UserService;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/users")
public class UserRestController {

	@Autowired
	UserService userService;

	@Autowired
	ModelMapper modelMapper;

	@GetMapping()
	public ResponseEntity<List<UserDTO>> getList() {
		List<User> users = userService.findAll();

		// Sử dụng modelMapper để ánh xạ danh sách User sang danh sách UserDTO
		ModelMapper modelMapper = new ModelMapper();
		List<UserDTO> userDtos = users.stream().map(user -> modelMapper.map(user, UserDTO.class))
				.collect(Collectors.toList());

		return ResponseEntity.ok(userDtos); // ResponseEntity.ok() tương đương HttpStatus.OK
	}

	@GetMapping("{userid}")
	public ResponseEntity<UserDTO> getOne(@PathVariable("userid") @Valid Integer userid, BindingResult bindingResult)
			throws UnkownIdentifierException {
		if (bindingResult.hasErrors()) {
			// Trả về mã trạng thái 400 Bad Request nếu có lỗi validation
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		User user = userService.findById(userid);

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		UserDTO userDTO = modelMapper.map(user, UserDTO.class);

		return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}

	@GetMapping("email/{useremail}")
	public ResponseEntity<UserDTO> getByemail(@PathVariable("useremail") String useremail)
			throws UnkownIdentifierException {
		User user = userService.findByEmail(useremail);

		if (user == null) {
			// Trả về mã trạng thái 404 Not Found nếu không tìm thấy category
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// Sử dụng modelMapper để ánh xạ từ User sang UserDTO
		UserDTO userDTO = modelMapper.map(user, UserDTO.class);

		// Trả về UserDTO bằng ResponseEntity với mã trạng thái 200 OK
		return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<UserDTO> create(@Valid @RequestBody User user) throws UserAlreadyExistException {
		User createdUser = userService.create(user);

		if (createdUser == null) {
			// Nếu không thể tạo User, trả về mã trạng thái 500 Internal Server Error
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		// Sử dụng ModelMapper để ánh xạ từ User sang UserDTO
		UserDTO userDTO = modelMapper.map(createdUser, UserDTO.class);

		// Trả về UserDTO bằng ResponseEntity với mã trạng thái 201 Created
		return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
	}

	@PutMapping("{userid}")
	public ResponseEntity<UserDTO> update(@PathVariable("userid") Integer userid, @RequestBody User user)
			throws UnkownIdentifierException {
		User updatedUser = userService.update(user);

		if (updatedUser == null) {
			// Trả về mã trạng thái 404 Not Found nếu không tìm thấy User
			return ResponseEntity.notFound().build();
		}

		UserDTO updatedUserDTO = modelMapper.map(updatedUser, UserDTO.class);
		
		// Trả về updatedUserDTO bằng ResponseEntity với mã trạng thái 200 OK
		return new ResponseEntity<>(updatedUserDTO, HttpStatus.OK);
	}

	@DeleteMapping("{userid}")
	public ResponseEntity<Void> delete(@PathVariable("userid") Integer userid) throws UnkownIdentifierException {

		User existingUser = userService.findById(userid);

		if (existingUser == null) {
			// Trả về mã trạng thái 404 Not Found nếu không tìm thấy user
			return ResponseEntity.notFound().build();
		}

		// Thực hiện xóa trong service
		userService.delete(userid);

		// Trả về mã trạng thái 204 No Content để chỉ ra thành công trong việc xóa
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/useradmin")
	public List<User> getAccounts(@RequestParam("admin") Optional<Boolean> admin) {
		if (admin.orElse(false)) {
			return userService.getAdministrators();
		}
		return userService.findAll();
	}

	// Tổng tiền mua hàng của các user
	@GetMapping("/total-purchase")
	public ResponseEntity<List<Report>> getTotalPurchaseByUser() {
		List<Report> totalPurchaseList = userService.getTotalPurchaseByUser();
		return new ResponseEntity<>(totalPurchaseList, HttpStatus.OK);
	}

}
