package com.greenfarm.cloudinary;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.greenfarm.cloudinary.CloudinaryService;

@RestController
@RequestMapping("/api/images")
public class ImageRestController {

	@Autowired
	private CloudinaryService cloudinaryService;

//    @PostMapping("/upload")
//    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
//        return cloudinaryService.uploadImage(file);
//    }
	@PostMapping("/upload")
	@ResponseBody
	public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file)
			throws IOException {
		String imageUrl = cloudinaryService.uploadImage(file);

		Map<String, String> response = new HashMap<>();
		response.put("imageUrl", imageUrl);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
