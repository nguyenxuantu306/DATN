package com.greenfarm.restcontroller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.greenfarm.dto.TourDateDTO;
import com.greenfarm.entity.CategorySalesByDate;
import com.greenfarm.entity.TourDate;
import com.greenfarm.entity.TourdateByDate;
import com.greenfarm.service.TourDateService;

import org.modelmapper.ModelMapper;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/tourdates")
public class TourDateRestController {

	@Autowired
	TourDateService toudateService;

	@Autowired
	ModelMapper modelMapper;

	@GetMapping()
	public ResponseEntity<List<TourDateDTO>> getList() {
		List<TourDate> tourdates = toudateService.findAll();

		// Sử dụng modelMapper để ánh xạ danh sách TourDate sang danh sách TourDateDTO
		ModelMapper modelMapper = new ModelMapper();
		List<TourDateDTO> tourdateDtos = tourdates.stream()
				.map(tourdate -> modelMapper.map(tourdate, TourDateDTO.class)).collect(Collectors.toList());

		return ResponseEntity.ok(tourdateDtos); // ResponseEntity.ok() tương đương HttpStatus.OK
	}

	@GetMapping("{tourdateid}")
	public ResponseEntity<TourDateDTO> getOne(@PathVariable("tourdateid") Integer tourdateid) {
		TourDate tourdate = toudateService.findById(tourdateid);

		if (tourdate == null) {
			// Trả về mã trạng thái 404 Not Found nếu không tìm thấy category
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// Sử dụng modelMapper để ánh xạ từ TourDate sang TourDateDTO
		ModelMapper modelMapper = new ModelMapper();
		TourDateDTO tourdateDTO = modelMapper.map(tourdate, TourDateDTO.class);

		// Trả về TourDateDTO bằng ResponseEntity với mã trạng thái 200 OK
		return new ResponseEntity<>(tourdateDTO, HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<TourDateDTO> create(@RequestBody TourDate tourdate) {
		TourDate createdTourDate = toudateService.create(tourdate);

		if (createdTourDate == null) {
			// Nếu không thể tạo TourDate, trả về mã trạng thái 500 Internal Server Error
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		// Sử dụng ModelMapper để ánh xạ từ TourDate sang TourDateDTO
		ModelMapper modelMapper = new ModelMapper();
		TourDateDTO tourdateDTO = modelMapper.map(createdTourDate, TourDateDTO.class);

		// Trả về TourDateDTO bằng ResponseEntity với mã trạng thái 201 Created
		return ResponseEntity.status(HttpStatus.CREATED).body(tourdateDTO);
	}

	@PutMapping("{tourdateid}")
	public ResponseEntity<TourDateDTO> update(@PathVariable("tourdateid") Integer tourdateid,
			@RequestBody TourDate tourdate) {

		// Thực hiện cập nhật trong service
		TourDate updatedTourDateResult = toudateService.update(tourdate);

		if (updatedTourDateResult == null) {
			// Trả về mã trạng thái 404 Not Found nếu không tìm thấy product để cập nhật
			return ResponseEntity.notFound().build();
		}

		// Sử dụng ModelMapper để ánh xạ từ Product đã cập nhật thành ProductDTO
		TourDateDTO tourdateDTO = modelMapper.map(updatedTourDateResult, TourDateDTO.class);

		// Trả về updatedTourDateDTO bằng ResponseEntity với mã trạng thái 200 OK
		return new ResponseEntity<>(tourdateDTO, HttpStatus.OK);
	}

	@DeleteMapping("/{tourdateid}")
	public ResponseEntity<Void> deletetourdate(@PathVariable("tourdateid") Integer tourdateid) {
		toudateService.deleteTourDateById(tourdateid);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/searchkeywordtourdate")
	public ResponseEntity<List<TourDateDTO>> getList(
	        @RequestParam(required = false) String keyword) {

	    List<TourDate> tourdates;

	    if (keyword != null) {
	        // Nếu ít nhất một trong hai tham số được cung cấp, thực hiện tìm kiếm
	        tourdates = toudateService.findByKeywordAndTourName(keyword);
	    } else {
	        // Nếu không có tham số nào được cung cấp, lấy tất cả các tourdate
	        tourdates = toudateService.findAll();
	    }

	    List<TourDateDTO> tourdateDtos = tourdates.stream()
	            .map(tourdate -> modelMapper.map(tourdate, TourDateDTO.class))
	            .collect(Collectors.toList());

	    return ResponseEntity.ok(tourdateDtos);
	}


	@GetMapping("/filtertourdate")
	public ResponseEntity<List<TourDateDTO>> getList(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
		List<TourDate> tourdates;

		if (date != null) {
			// Nếu chỉ có ngày, thực hiện tìm kiếm theo ngày
			tourdates = toudateService.findByDate(date);
		} else {
			// Nếu không có ngày, lấy tất cả
			tourdates = toudateService.findAll();
		}

		List<TourDateDTO> tourdateDtos = tourdates.stream()
				.map(tourdate -> modelMapper.map(tourdate, TourDateDTO.class)).collect(Collectors.toList());

		return ResponseEntity.ok(tourdateDtos);
	}

	@GetMapping("/today")
	public ResponseEntity<List<TourDateDTO>> getListtoday() {
		List<TourDate> tourdates;
		Date date = new Date();
		tourdates = toudateService.findByDate(date);
		

		List<TourDateDTO> tourdateDtos = tourdates.stream()
				.map(tourdate -> modelMapper.map(tourdate, TourDateDTO.class)).collect(Collectors.toList());

		return ResponseEntity.ok(tourdateDtos);
	}
	
	@GetMapping("/getTourByDate")
	public ResponseEntity<List<TourdateByDate>> getTourByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
	    List<TourdateByDate> result = toudateService.getTourByDate(date);
	    return ResponseEntity.ok(result);
	}
	
	


}
