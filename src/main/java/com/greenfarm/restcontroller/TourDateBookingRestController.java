package com.greenfarm.restcontroller;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.greenfarm.dto.TourDateBookingDTO;
import com.greenfarm.entity.TourDateBooking;
import com.greenfarm.service.TourDateBookingService;

import org.modelmapper.ModelMapper;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/tourdatebookings")
public class TourDateBookingRestController {

	@Autowired
	TourDateBookingService toudatebookingService;

	@Autowired
	ModelMapper modelMapper;

	@GetMapping()
	public ResponseEntity<List<TourDateBookingDTO>> getList() {
		List<TourDateBooking> tourdatebookings = toudatebookingService.findAll();

		// Sử dụng modelMapper để ánh xạ danh sách TourDateBooking sang danh sách TourDateBookingBookingDTO
		ModelMapper modelMapper = new ModelMapper();
		List<TourDateBookingDTO> tourdatebookingDtos = tourdatebookings.stream()
				.map(tourdatebooking -> modelMapper.map(tourdatebooking, TourDateBookingDTO.class)).collect(Collectors.toList());

		return ResponseEntity.ok(tourdatebookingDtos); // ResponseEntity.ok() tương đương HttpStatus.OK
	}

	@GetMapping("{tourdatebookingid}")
	public ResponseEntity<TourDateBookingDTO> getOne(@PathVariable("tourdatebookingid") Integer tourdatebookingid) {
		TourDateBooking tourdatebooking = toudatebookingService.findById(tourdatebookingid);

		if (tourdatebooking == null) {
			// Trả về mã trạng thái 404 Not Found nếu không tìm thấy category
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// Sử dụng modelMapper để ánh xạ từ TourDateBooking sang TourDateBookingBookingDTO
		ModelMapper modelMapper = new ModelMapper();
		TourDateBookingDTO tourdatebookingDTO = modelMapper.map(tourdatebooking, TourDateBookingDTO.class);

		// Trả về TourDateBookingBookingDTO bằng ResponseEntity với mã trạng thái 200 OK
		return new ResponseEntity<>(tourdatebookingDTO, HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<TourDateBookingDTO> create(@RequestBody TourDateBooking toudatebooking) {
		TourDateBooking createdTourDateBooking = toudatebookingService.create(toudatebooking);

		if (createdTourDateBooking == null) {
			// Nếu không thể tạo TourDateBooking, trả về mã trạng thái 500 Internal Server Error
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		// Sử dụng ModelMapper để ánh xạ từ TourDateBooking sang TourDateBookingBookingDTO
		ModelMapper modelMapper = new ModelMapper();
		TourDateBookingDTO toudatebookingDTO = modelMapper.map(createdTourDateBooking, TourDateBookingDTO.class);

		// Trả về TourDateBookingBookingDTO bằng ResponseEntity với mã trạng thái 201 Created
		return ResponseEntity.status(HttpStatus.CREATED).body(toudatebookingDTO);
	}

	@PutMapping("{tourdatebookingid}")
	public ResponseEntity<TourDateBookingDTO> update(@PathVariable("tourdatebookingid") Integer tourdatebookingid,
			@RequestBody TourDateBooking tourdatebooking) {

		// Thực hiện cập nhật trong service
		TourDateBooking updatedTourDateBookingResult = toudatebookingService.update(tourdatebooking);

		if (updatedTourDateBookingResult == null) {
			// Trả về mã trạng thái 404 Not Found nếu không tìm thấy product để cập nhật
			return ResponseEntity.notFound().build();
		}
		
		
		// Sử dụng ModelMapper để ánh xạ từ Product đã cập nhật thành ProductDTO
		TourDateBookingDTO tourdatebookingDTO = modelMapper.map(updatedTourDateBookingResult, TourDateBookingDTO.class);

		// Trả về updatedTourDateBookingBookingDTO bằng ResponseEntity với mã trạng thái 200 OK
		return new ResponseEntity<>(tourdatebookingDTO, HttpStatus.OK);
	}


	@DeleteMapping("/{tourdatebookingid}")
	public ResponseEntity<Void> deletetourdate(@PathVariable("tourdatebookingid") Integer tourdatebookingid) {
		toudatebookingService.deleteTourDateBookingById(tourdatebookingid);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
//	@GetMapping("/searchkeywordcategory")
//	public ResponseEntity<List<TourDateBookingBookingDTO>> getList(@RequestParam(required = false) String keyword) {
//		List<TourDateBooking> categorys;
//
//		if (keyword != null && !keyword.isEmpty()) {
//			// Nếu có từ khóa, thực hiện tìm kiếm
//			categorys = toudateService.findByKeyword(keyword);
//		} else {
//			// Nếu không có từ khóa, lấy tất cả người dùng
//			categorys = toudateService.findAll();
//		}
//
//		List<TourDateBookingBookingDTO> categoryDtos = categorys.stream().map(category -> modelMapper.map(category, TourDateBookingBookingDTO.class))
//				.collect(Collectors.toList());
//
//		return ResponseEntity.ok(categoryDtos);
//	}
}
