package com.greenfarm.cloudinary;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {

	private Cloudinary cloudinary;

	public CloudinaryService() {
		cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", "dqpa9elwm", "api_key", "652362731123681",
				"api_secret", "5hJIdaqX6YtyNAPgO5A706zlEks"));
	}

	public String uploadImage(MultipartFile file) throws IOException {
		Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
		return (String) uploadResult.get("secure_url");
	}

	public String uploadQRCode(String base64QRCode) throws IOException {
		// Decode base64 string to bytes
		byte[] qrCodeBytes = Base64.getDecoder().decode(base64QRCode);

		// Upload QR code to Cloudinary
		Map uploadResult = cloudinary.uploader().upload(qrCodeBytes, ObjectUtils.emptyMap());

		// Return the secure URL of the uploaded QR code
		return (String) uploadResult.get("secure_url");
	}
}
