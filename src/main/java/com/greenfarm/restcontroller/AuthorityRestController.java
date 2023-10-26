package com.greenfarm.restcontroller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenfarm.entity.UserRole;
import com.greenfarm.service.UserRoleService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/authorities")
public class AuthorityRestController {

	@Autowired
	UserRoleService userRoleService;

	@GetMapping
	public List<UserRole> findAll(@RequestParam("admin") Optional<Boolean> admin) {
		if (admin.orElse(false)) {
			return userRoleService.findAuthoritesOfAdministrators();
		}
		return userRoleService.findAll();
	}

	@PostMapping
	public UserRole post(@RequestBody UserRole auth) {
		return userRoleService.create(auth);
	}

	@DeleteMapping("{userroleid}")
	public void delete(@PathVariable("userroleid") Integer id) {
		userRoleService.delete(id);
	}

}
