package com.greenfarm.restcontroller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64; // Đối với decodeBase64
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.greenfarm.dto.BookingDTO;
import com.greenfarm.entity.Booking;
import com.greenfarm.entity.StatusBooking;
import com.greenfarm.service.BookingService;
import com.greenfarm.service.StatusBookingService;


@RestController
public class ScanticketRestController {	
	
	@Autowired
	ModelMapper modelMapper;
	List<String> ketqua = new ArrayList<>();
	
	
	@PostMapping("/capture")
	public ResponseEntity<String> captureImage(@RequestBody Map<String, String> requestData) {
	    try {
	        if (!requestData.containsKey("imageData")) {
	            return ResponseEntity.badRequest().body("Missing imageData field");
	        }

	        String imageData = requestData.get("imageData");
	        System.out.println(imageData);
	        // Chuyển đổi base64 thành BufferedImage
	        byte[] imageBytes = Base64.decodeBase64(imageData.split(",")[1]);
	        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes)) {
	            BufferedImage bufferedImage = ImageIO.read(bis);
	            
	            // Thực hiện xử lý QR code
	            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
	            Result result = new MultiFormatReader().decode(bitmap);

	            String qrCodeValue = result.getText();
	            ketqua.add(qrCodeValue);
	            System.out.println("quet thanh cong");
	            System.out.println(qrCodeValue);
	            System.out.println("list"+ketqua);
	            // ResponseEntity.ok(qrCodeValue);
	           
	            return new ResponseEntity<>(qrCodeValue, HttpStatus.OK);
	            
	        }
	    } catch (NotFoundException e) {
	        // Xử lý khi không tìm thấy mã QR code
	    	System.out.println("\"QR code not found in the image.\"");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("QR code not found in the image.");
	    } catch (Exception e) {
	        // Xử lý các ngoại lệ khác
	        e.printStackTrace();
	        System.out.println("lỗi ");
	        // Cung cấp thông báo lỗi chi tiết
	        String errorMessage = "Error capturing and decoding QR code: " + e.getMessage();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(errorMessage);
	    }

	}

	@Autowired
	StatusBookingService statusBookingService;
	
	@GetMapping("/soatve/kiemtrave")
	public ResponseEntity<Booking> updatekiemtrave( Model model
			) {
		
		ketqua.add("6");
		ketqua.add("101");
		if (!ketqua.isEmpty()) {
            // Lấy phần tử cuối cùng sử dụng phương thức get(size() - 1)
            String phanTuCuoiCung = ketqua.get(ketqua.size() - 1);
            System.out.println("Phần tử cuối cùng: " + phanTuCuoiCung);
        } else {
            System.out.println("Danh sách rỗng");
        }
		// Kiểm tra quyền nếu là Admin -> chuyển trạng thái
		
		
			Booking booking = bookingService.findById(1);
			List<Booking> list = new ArrayList<>();
			list.add(booking);
			if (booking.getStatusbooking().getStatusbookingid() == 5) {
				System.out.println("Vé đã được sử dụng");
				// Trả về giao diện Thymeleaf khi vé đã được sử dụng
				
				// Thêm dữ liệu vào model nếu cần
//				model.addAttribute("bookinguse", booking);
				
				return new ResponseEntity<>(booking, HttpStatus.OK);
			} else {
				StatusBooking statusBooking = statusBookingService.findById(5);
				booking.setStatusbooking(statusBooking);
				Booking updatedBooking = bookingService.update(booking);
				System.out.println("Đã xác nhận thành công");
				// Trả về giao diện Thymeleaf khi xác nhận thành công
				ModelAndView mav = new ModelAndView("ticket");
				// Thêm dữ liệu vào model nếu cần
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
				
			}
		} 
	
	@Autowired
	BookingService bookingService;
	
	@GetMapping("/danhsachsoat")
	public ResponseEntity<List<BookingDTO>> getList() {
		List<Booking> bookings = bookingService.findAll();

		// Sử dụng ModelMapper để ánh xạ từ danh sách Product sang danh sách ProductDTO
		List<BookingDTO> BookingsDTOs = bookings.stream().map(booking -> modelMapper.map(booking, BookingDTO.class))
				.collect(Collectors.toList());

		// Trả về danh sách ProductDTO bằng ResponseEntity với mã trạng thái 200 OK
		return new ResponseEntity<>(BookingsDTOs, HttpStatus.OK);
	}
//
//
//	@PostMapping("/capture")
//	public ResponseEntity<String> captureImage(@RequestBody Map<String, String> requestData) {
//		try {
//			String imageData = requestData.get("imageData");
//
//			// Chuyển đổi base64 thành BufferedImage
//            byte[] imageBytes = Base64.decodeBase64(imageData.split(",")[1]);
//            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
//            BufferedImage bufferedImage = ImageIO.read(bis);
//
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			ImageIO.write(bufferedImage, "png", baos);
//			baos.flush();
//
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.IMAGE_PNG);
//
//			BinaryBitmap bitmap = new BinaryBitmap(
//					new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
//			Result result = new MultiFormatReader().decode(bitmap);
//
//			String qrCodeValue = result.getText();
//
//			 //webcam.close();
//
//			return ResponseEntity.ok(qrCodeValue);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
//					.body("Error capturing and decoding QR code.");
//		}
//	}

}
