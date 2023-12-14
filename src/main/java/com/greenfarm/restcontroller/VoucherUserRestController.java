package com.greenfarm.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenfarm.service.VoucherUserService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/vouchers")
public class VoucherUserRestController {
	@Autowired
	private VoucherUserService voucherUserService;

	@GetMapping("/user/check")
	public CheckResponse checkUserVoucherExistence(@RequestParam("userid") Integer userId,
			@RequestParam("voucherid") Integer voucherId) {

		boolean exists = voucherUserService.existsByUserIdAndVoucherId(userId, voucherId);

		return new CheckResponse(exists);
	}

	public static class CheckResponse {
		private boolean exists;

		public CheckResponse(boolean exists) {
			this.exists = exists;
		}

		public boolean isExists() {
			return exists;
		}
	}
}
