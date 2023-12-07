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

import com.greenfarm.dto.TourDateBookingDTO;
import com.greenfarm.dto.TourDateDTO;
import com.greenfarm.entity.TourDate;
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
	
	@GetMapping("/searchkeywordtourdatebooking")
	public ResponseEntity<List<TourDateBookingDTO>> getList(@RequestParam(required = false) String keyword) {
		List<TourDateBooking> tourdatebookings;

		if (keyword != null && !keyword.isEmpty()) {
			// Nếu có từ khóa, thực hiện tìm kiếm
			tourdatebookings = toudatebookingService.findByKeyword(keyword);
		} else {
			// Nếu không có từ khóa, lấy tất cả người dùng
			tourdatebookings = toudatebookingService.findAll();
		}

		List<TourDateBookingDTO> tourdatebookingDtos = tourdatebookings.stream().map(tourdatebooking -> modelMapper.map(tourdatebooking, TourDateBookingDTO.class))
				.collect(Collectors.toList());

		return ResponseEntity.ok(tourdatebookingDtos);
	}
	
	@GetMapping("/searchbydepartureday")
    public ResponseEntity<List<TourDateBookingDTO>> getListdepartureday(@RequestParam(required = false) String departureday) {
        List<TourDateBooking> tourdatebookings;

        if (departureday != null && !departureday.isEmpty()) {
            // Nếu có departureday, thực hiện lọc theo departureday
            tourdatebookings = toudatebookingService.findByDepartureDay(departureday);
        } else {
            // Nếu không có departureday, lấy tất cả các TourDateBooking
            tourdatebookings = toudatebookingService.findAll();
        }

        List<TourDateBookingDTO> tourdatebookingDtos = tourdatebookings.stream()
                .map(tourdatebooking -> modelMapper.map(tourdatebooking, TourDateBookingDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(tourdatebookingDtos);
    }
	
//	@GetMapping("/filtertourdate")
//	public ResponseEntity<List<TourDateBookingDTO>> getList(
//	        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
//	    List<TourDateBooking> tourdatebookings;
//
//	    if (date != null) {
//	        // Nếu chỉ có ngày, thực hiện tìm kiếm theo ngày
//	    	tourdatebookings = toudatebookingService.findByDate(date);
//	    } else {
//	        // Nếu không có ngày, lấy tất cả
//	    	tourdatebookings = toudatebookingService.findAll();
//	    }
//
//	    List<TourDateBookingDTO> tourdatebookingDtos = tourdatebookings.stream()
//	            .map(tourdatebooking -> modelMapper.map(tourdatebooking, TourDateBookingDTO.class))
//	            .collect(Collectors.toList());
//
//	    return ResponseEntity.ok(tourdatebookingDtos);
//	}

}
