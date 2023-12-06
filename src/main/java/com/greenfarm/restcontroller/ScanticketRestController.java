package com.greenfarm.restcontroller;

import org.springframework.web.bind.annotation.RestController;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Map;
import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64; // Đối với decodeBase64
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;


@RestController
public class ScanticketRestController {	
	
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
	            System.out.println("quet thanh cong");
	            System.out.println(qrCodeValue);
	            return ResponseEntity.ok(qrCodeValue);
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
