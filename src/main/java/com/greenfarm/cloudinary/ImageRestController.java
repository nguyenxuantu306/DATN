package com.greenfarm.cloudinary;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.greenfarm.service.BookingService;
import com.greenfarm.service.QRCodeService;

@RestController
@RequestMapping("/api/images")
public class ImageRestController {

	@Autowired
	private CloudinaryService cloudinaryService;
	
	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private QRCodeService qrCodeService;

	@PostMapping("/upload")
	@ResponseBody
	public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file)
			throws IOException {
		String imageUrl = cloudinaryService.uploadImage(file);

		Map<String, String> response = new HashMap<>();
		response.put("imageUrl", imageUrl);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	
	@PostMapping("/uploadQRCode")
	@ResponseBody
	public String uploadQRCodeToCloud(@RequestBody String qrCodeContent) throws IOException {
		// Generate and save QR code to cloud
		byte[] qrCode = qrCodeService.generateQRCode(qrCodeContent, 500, 500);
		String base64QRCode = Base64.getEncoder().encodeToString(qrCode);
		String qrCodeUrl = cloudinaryService.uploadQRCode(base64QRCode);

		return qrCodeUrl;
	}

}
