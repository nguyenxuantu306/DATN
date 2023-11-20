package com.greenfarm.cloudinary;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/upload")
public class ImageController {

	@Autowired
	private CloudinaryService cloudinaryService;

	@GetMapping
	public String showUploadForm() {
		return "upload";
	}

	@PostMapping("/api/images/upload")
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
	    try {
	        String imageUrl = cloudinaryService.uploadImage(file);
	        return ResponseEntity.ok(imageUrl);
	    } catch (IOException e) {
	        // Xử lý lỗi tải lên ảnh
	        return ResponseEntity.status(500).body("Error uploading image");
	    }
	}
}
