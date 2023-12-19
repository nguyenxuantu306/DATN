package com.greenfarm.restcontroller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

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
import com.greenfarm.utils.Log;


@RestController
public class ScanticketRestController {	
	
	@Autowired
	ModelMapper modelMapper;
	List<String> ketqua = new ArrayList<>();
	
	  public static String decode(String encodedText) {
	        byte[] decodedBytes = Base64.getDecoder().decode(encodedText);
	        return new String(decodedBytes);
	    }
	
	@PostMapping("/capture")
	public ResponseEntity<String> captureImage(@RequestBody Map<String, String> requestData) {
	    try {
	        if (!requestData.containsKey("imageData")) {
	        	Log.warn("Missing QRCode imageData field");
	            return ResponseEntity.badRequest().body("Missing imageData field");
	        }

	        String imageData = requestData.get("imageData");
	        System.out.println(imageData);
	        // Chuyển đổi base64 thành BufferedImage
	        byte[] imageBytes = org.apache.commons.codec.binary.Base64.decodeBase64(imageData.split(",")[1]);
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
	            //giai ma
	            String decodedText = decode(qrCodeValue);
	            System.out.println("sau khi giai ma ve:");
	           System.out.println("ket qua qr tra ve :"+decodedText);
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
	
	@GetMapping("/soatve/kiemtrave/{bookingid}")
	public ResponseEntity<BookingDTO> updatekiemtrave( @PathVariable("bookingid") Integer bookingid,Model model) {
		
		
		Booking booking = bookingService.findById(bookingid);
		BookingDTO bookingDTOs = modelMapper.map(booking, BookingDTO.class);
		return new ResponseEntity<>(bookingDTOs, HttpStatus.OK);
//		Booking booking = bookingService.findById(bookingid);
//		return new ResponseEntity<>(booking, HttpStatus.OK);
		
//		if (booking.getStatusbooking().getStatusbookingid() == 5) {
//			System.out.println("Vé đã được sử dụng");
//			return new ResponseEntity<>(booking, HttpStatus.OK);
//			
//		} else {
//			StatusBooking statusBooking = statusBookingService.findById(5);
//			booking.setStatusbooking(statusBooking);
//			Booking updatedBooking = bookingService.update(booking);
//			System.out.println("Đã xác nhận thành công");
//			// Trả về giao diện Thymeleaf khi xác nhận thành công
//			ModelAndView mav = new ModelAndView("mytiecketuse");
//			// Thêm dữ liệu vào model nếu cần
//			model.addAttribute("bookinguse", booking);
//			mav.addObject("message", "Đã xác nhận thành công");
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
			
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
	
	@PostMapping("/checktikcet")
	public ResponseEntity<?> captureImage(Model model,@RequestBody Map<String, String> requestData) {
	    try {
	        if (!requestData.containsKey("imageData")) {
	        	Log.warn("Missing QRCode imageData field");
	            return ResponseEntity.badRequest().body("Missing imageData field");
	        }

	        String imageData = requestData.get("imageData");
	        System.out.println(imageData);
	        // Chuyển đổi base64 thành BufferedImage
	        byte[] imageBytes = org.apache.commons.codec.binary.Base64.decodeBase64(imageData.split(",")[1]);
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
	            //giai ma
	            String decodedText = decode(qrCodeValue);
	            System.out.println("sau khi giai ma ve:");
	           System.out.println("ket qua qr tra ve :"+decodedText);
	           
	           try {
	        	   Integer bookingid = Integer.parseInt(decodedText);
	        	   Booking booking = bookingService.findById(bookingid);
	   	   		

	   	   	if (booking.getStatusbooking().getStatusbookingid() == 5) {
	   	   	BookingDTO bookingDTOs = modelMapper.map(booking, BookingDTO.class);
	   	 return new ResponseEntity<>(bookingDTOs, HttpStatus.OK);
				
			} else if(booking.getStatusbooking().getStatusbookingid() == 2 && booking.getTourDateBooking().getTourdate().getTourdates()  == new Date()) {
				StatusBooking statusBooking = statusBookingService.findById(5);
				booking.setStatusbooking(statusBooking);
				//booking.setUsedate(LocalDateTime.now());
				booking.setUsedate(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime());

				Booking updatedBooking = bookingService.update(booking);
				
				BookingDTO bookingDTOs = modelMapper.map(booking, BookingDTO.class);
				Log.info("Đã xác nhận thành công");
				return new ResponseEntity<>(bookingDTOs, HttpStatus.OK);
				
			}else {
				BookingDTO bookingDTOs = modelMapper.map(booking, BookingDTO.class);
			   	 return new ResponseEntity<>(bookingDTOs, HttpStatus.OK);
			}
	        	    
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				  return ResponseEntity.badRequest().body("Khong Lay Duoc Booking");
			}
	           
	            
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

}
