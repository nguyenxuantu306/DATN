package com.greenfarm.restcontroller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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

import com.greenfarm.dto.ProductDTO;
import com.greenfarm.dto.VoucherDTO;
import com.greenfarm.dto.VoucherUserDTO;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.User;
import com.greenfarm.entity.Voucher;
import com.greenfarm.entity.VoucherUser;
import com.greenfarm.exception.UnkownIdentifierException;
import com.greenfarm.service.VoucherService;
import com.greenfarm.service.VoucherUserService;
import com.greenfarm.utils.Log;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/vouchers")
public class VoucherRestController {
	@Autowired
	ModelMapper modelMapper;

	@Autowired
	VoucherService voucherService;

	@Autowired
	VoucherUserService voucheruserservice;

	@GetMapping()
	public ResponseEntity<List<VoucherDTO>> getList() {
		List<Voucher> vouchers = voucherService.findAll();
		List<VoucherDTO> voucherDTOs = vouchers.stream().map(voucher -> modelMapper.map(voucher, VoucherDTO.class))
				.collect(Collectors.toList());
		return new ResponseEntity<>(voucherDTOs, HttpStatus.OK);
	}

	// Lấy ra danh sách đã xóa
	@GetMapping("/deleted")
	public ResponseEntity<List<VoucherDTO>> getDeletedList() {
		try {
			Log.info("Đã nhận được yêu cầu cho danh sách getDeletedList");
			List<Voucher> deletedVouchers = voucherService.findAllDeletedVouchers();

			// Sử dụng ModelMapper để ánh xạ từ danh sách Voucher sang danh sách VoucherDTO
			List<VoucherDTO> voucherDTOs = deletedVouchers.stream()
					.map(voucher -> modelMapper.map(voucher, VoucherDTO.class)).collect(Collectors.toList());

			// Trả về danh sách VoucherDTO bằng ResponseEntity với mã trạng thái 200 OK
			return new ResponseEntity<>(voucherDTOs, HttpStatus.OK);
		} catch (Exception e) {
			Log.error("Đã xảy ra lỗi trong getDeletedList", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("user")
	public ResponseEntity<List<VoucherUserDTO>> getListuser() {
		List<VoucherUser> voucherusers = voucheruserservice.findAll();
		List<VoucherUserDTO> voucheruserDTOs = voucherusers.stream()
				.map(voucheruser -> modelMapper.map(voucheruser, VoucherUserDTO.class)).collect(Collectors.toList());
		return new ResponseEntity<>(voucheruserDTOs, HttpStatus.OK);
	}

	@GetMapping("{voucherid}")
	public ResponseEntity<VoucherDTO> getOne(@PathVariable("voucherid") Integer voucherid) {
		Voucher voucher = voucherService.findById(voucherid);
		if (voucher == null) {
			return ResponseEntity.notFound().build();
		}
		VoucherDTO voucherDTO = modelMapper.map(voucher, VoucherDTO.class);
		return new ResponseEntity<>(voucherDTO, HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<VoucherDTO> create(@RequestBody Voucher voucher, Model model) {

		Voucher createdVoucher = voucherService.create(voucher);
		VoucherDTO voucherDTO = modelMapper.map(createdVoucher, VoucherDTO.class);
		return new ResponseEntity<>(voucherDTO, HttpStatus.CREATED);
	}

	@PostMapping("user")
	public ResponseEntity<VoucherUserDTO> createuser(@RequestBody VoucherUser voucheruser, Model model) {

		VoucherUser createdVoucheruser = voucherUserService.create(voucheruser);
		VoucherUserDTO voucherUserDTO = modelMapper.map(createdVoucheruser, VoucherUserDTO.class);
		return new ResponseEntity<>(voucherUserDTO, HttpStatus.CREATED);
	}

	@PutMapping("{voucherid}")
	public ResponseEntity<VoucherDTO> update(@PathVariable("voucherid") Integer voucherid,
			@RequestBody Voucher voucher) {
		Voucher updatedVoucher = voucherService.update(voucher);

		if (updatedVoucher == null) {
			return ResponseEntity.notFound().build();
		}
		VoucherDTO voucherDTO = modelMapper.map(updatedVoucher, VoucherDTO.class);
		return new ResponseEntity<>(voucherDTO, HttpStatus.OK);
	}

	@PutMapping("user/{voucheruserid}")
	public ResponseEntity<VoucherUserDTO> updateuser(@PathVariable("voucheruserid") Integer voucheruserid,
			@RequestBody VoucherUser voucheruser) {
		VoucherUser updatedVoucheruser = voucheruserservice.update(voucheruser);

		if (updatedVoucheruser == null) {
			return ResponseEntity.notFound().build();
		}
		VoucherUserDTO voucheruserDTO = modelMapper.map(updatedVoucheruser, VoucherUserDTO.class);
		return new ResponseEntity<>(voucheruserDTO, HttpStatus.OK);
	}

	// Khôi phục voucher
	@PutMapping("/{voucherid}/restore")
	public ResponseEntity<String> restoreVoucher(@PathVariable("voucherid") Integer voucherid) {
		try {
			Log.info("Nhận yêu cầu khôi phục vourcher với ID: {}", voucherid);

			// Tìm kiếm sản phẩm với id tương ứng trong cơ sở dữ liệu
			Voucher vourcher = voucherService.findById(voucherid);

			if (vourcher == null) {
				Log.warn("Không tìm thấy vourcher có ID {}. Không thể khôi phục.", voucherid);
				return new ResponseEntity<>("Sản phẩm không tồn tại", HttpStatus.NOT_FOUND);
			}

			// Khôi phục trạng thái đã xóa của sản phẩm
			vourcher.setIsDeleted(false);

			// Lưu sản phẩm đã khôi phục vào cơ sở dữ liệu
			voucherService.save(vourcher);

			Log.info("Voucher có ID {} đã được khôi phục thành công", voucherid);
			return new ResponseEntity<>("Khôi phục Voucher thành công", HttpStatus.OK);
		} catch (Exception e) {
			Log.error("Đã xảy ra lỗi khi khôi phục Voucher có ID: {}", voucherid, e);
			return new ResponseEntity<>("Lỗi trong quá trình xử lý", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("{voucherid}")
	public ResponseEntity<Void> delete(@PathVariable("voucherid") Integer voucherid) throws UnkownIdentifierException {

		Voucher existingUser = voucherService.findById(voucherid);

		if (existingUser == null) {
			// Trả về mã trạng thái 404 Not Found nếu không tìm thấy user
			return ResponseEntity.notFound().build();
		}

		// Thực hiện xóa trong service
		voucherService.delete(voucherid);

		// Trả về mã trạng thái 204 No Content để chỉ ra thành công trong việc xóa
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("user/{voucheruserid}")
	public ResponseEntity<Void> deleteuser(@PathVariable("voucheruserid") Integer voucheruserid) {
		VoucherUser existingVoucheruser = voucherUserService.findById(voucheruserid);

		if (existingVoucheruser == null) {
			return ResponseEntity.notFound().build();
		}
		voucherUserService.delete(voucheruserid);
		return ResponseEntity.noContent().build();
	}
	
	
	@GetMapping("searchkeywordvoucher")
	public ResponseEntity<List<VoucherDTO>> getList(@RequestParam(required = false) String keyword) {
		List<Voucher> vouchers;

		if (keyword != null && !keyword.isEmpty()) {
			// Nếu có từ khóa, thực hiện tìm kiếm
			vouchers = voucherService.findByKeyword(keyword);
		} else {
			// Nếu không có từ khóa, lấy tất cả người dùng
			vouchers = voucherService.findAll();
		}

		List<VoucherDTO> voucherDtos = vouchers.stream().map(voucher -> modelMapper.map(voucher, VoucherDTO.class))
				.collect(Collectors.toList());

		return ResponseEntity.ok(voucherDtos);
	}
	
	
	@GetMapping("user/searchkeywordvoucheruser")
	public ResponseEntity<List<VoucherUserDTO>> getListuser(@RequestParam(required = false) String keyword) {
		List<VoucherUser> voucherusers;

		if (keyword != null && !keyword.isEmpty()) {
			// Nếu có từ khóa, thực hiện tìm kiếm
			voucherusers = voucherUserService.findByKeyword(keyword);
		} else {
			// Nếu không có từ khóa, lấy tất cả người dùng
			voucherusers = voucherUserService.findAll();
		}

		List<VoucherUserDTO> voucherDtos = voucherusers.stream().map(voucheruser -> modelMapper.map(voucheruser, VoucherUserDTO.class))
				.collect(Collectors.toList());

		return ResponseEntity.ok(voucherDtos);
	}
//	@DeleteMapping("user/{voucheruserid}")
//	public ResponseEntity<Void> deleteuser(@PathVariable("voucheruserid") Integer voucheruserid) {
//		VoucherUser existingVoucheruser = voucheruserservice.findById(voucheruserid);
//
//		if (existingVoucheruser == null) {
//			return ResponseEntity.notFound().build();
//		}
//		voucheruserservice.delete(voucheruserid);
//		return ResponseEntity.noContent().build();
//	}

}
