package com.greenfarm.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.http.HttpHeaders;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.naming.spi.DirStateFactory.Result;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

@RestController
@RequestMapping("/api/camera")
@CrossOrigin(origins = "http://localhost:8080/")
public class CameraController {

//    @GetMapping("/capture")
//    public ResponseEntity<byte[]> capture() {
//        BufferedImage bufferedImage = webcamService.capture();
//
//        // Convert BufferedImage to byte array
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        try {
//            ImageIO.write(bufferedImage, "png", baos);
//            baos.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // Set the content type as image/png
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG);
//        
//
//        // Return the byte array as ResponseEntity
//        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
//    }
    
//    @GetMapping("/list")
//    public ResponseEntity<List<CameraInfo>> getCameraList() {
//        // Logic để lấy danh sách camera, sử dụng WebRTC API hoặc một nguồn thông tin khác
//        List<CameraInfo> cameraList = //...;
//
//        return ResponseEntity.ok(cameraList);
//    }
    
    @GetMapping
    public List<String> getCameraNames() {
        List<Webcam> webcams = Webcam.getWebcams();
        return webcams.stream()
                .map(Webcam::getName)
                .collect(Collectors.toList());
    }
    
//    @RequestMapping("/selectCamera")
//    public String selectCamera(@RequestParam String cameraName) {
//        // Xử lý yêu cầu chọn camera ở đây
//        // Bạn có thể sử dụng cameraName để xác định camera được chọn
//
//        // Trả về một giá trị nào đó, có thể là đường dẫn đến video stream của camera
//        return "redirect:/videoStream?cameraName=" + cameraName;
//    }
//
//    // Controller để xử lý video stream từ camera đã chọn
//    // Bạn cần triển khai logic xử lý video stream ở đây
//
//    @RequestMapping("/videoStream")
//    public String videoStream(@RequestParam String cameraName) {
//        // Logic xử lý video stream từ camera đã chọn ở đây
//        // Đây có thể là endpoint để trả về video stream
//
//        return "video_stream_page"; // Tên trang HTML hoặc đường dẫn tới video stream
//    }
    
    @GetMapping("/dongcamera")
    public String capturedong(Model model) {
    	List<Webcam> webcams = Webcam.getWebcams();
    	model.addAttribute("listcamera", webcams);
System.out.println(webcams);

    	// Chọn camera đầu tiên trong danh sách
    	Webcam selectedWebcam = webcams.get(0);
    	System.out.println(selectedWebcam);
    	
    	// Đóng camera hiện tại
    	
    	//webcam.close();

    	// Mở camera mới
    	selectedWebcam.open(false);

    return null;
    }
    
    @GetMapping("/captureqr")
    public String capture1(Model model) {
    	
    	// Lấy danh sách các camera
    	List<Webcam> webcams = Webcam.getWebcams();
    	model.addAttribute("listcamera", webcams);
        System.out.println(webcams);

    	// Chọn camera đầu tiên trong danh sách
    	Webcam selectedWebcam = webcams.get(0);
    	System.out.println(selectedWebcam);
    	
    	// Đóng camera hiện tại
    	
    	//webcam.close();
    }
    
	@GetMapping("/soatve")
	public String soatve() {

		return null;
	}

	@GetMapping("/captureqr")
	public String capture1() {

		// Lấy danh sách các camera
		List<Webcam> webcams = Webcam.getWebcams();
		System.out.println(webcams);
		// Chọn camera đầu tiên trong danh sách
		Webcam selectedWebcam = webcams.get(0);
		System.out.println(selectedWebcam);

		// Đóng camera hiện tại

		// webcam.close();

		// Mở camera mới
		selectedWebcam.open(true);

		// Đổi sang camera khác (ví dụ: camera thứ hai)
		// selectedWebcam = webcams.get(1);

		try {
			// BufferedImage bufferedImage = webcamService.capture();
			BufferedImage bufferedImage = selectedWebcam.getImage();

			// Convert BufferedImage to byte array
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", baos);
			baos.flush();

			// Set the content type as image/png
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_PNG);

			// Return the byte array as ResponseEntity

//            // Decode QR code from the captured image
//            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(
//                    new BufferedImageLuminanceSource(bufferedImage)));
			BinaryBitmap bitmap = new BinaryBitmap(
					new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));

			Result result = new MultiFormatReader().decode(bitmap);

			// You can use the result.getText() to get the decoded QR code value
			String qrCodeValue = result.getText();

            // You can return the decoded QR code value as part of the response
            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
           // return "redirect:/"+qrCodeValue;
            return "/readqrcode"; 
           // return ResponseEntity.ok(qrCodeValue);
        } catch (Exception e) {
            e.printStackTrace();
            //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error capturing and decoding QR code.");
            System.out.println("loi");
            return "/readqrcode"; 
        }
    }
    
    
    // This endpoint captures an image from the webcam, decodes a QR code, and returns the decoded value
    @GetMapping("/scanticket")
    public ResponseEntity<String> captureQrCode(Webcam webcam) {
        try {
            // Replace this with the logic to capture an image from the webcam
            BufferedImage bufferedImage = webcam.getImage(); // Assuming selectedWebcam is defined

            // Convert BufferedImage to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            baos.flush();

            // Set the content type as image/png
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            // Decode QR code from the captured image
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
            Result result = new MultiFormatReader().decode(bitmap);

            // You can use the result.getText() to get the decoded QR code value
            String qrCodeValue = result.getText();

            // Perform any action with the decoded QR code value
            System.out.println("Decoded QR Code: " + qrCodeValue);

            // You can return the decoded QR code value as part of the response
            return ResponseEntity.ok(qrCodeValue);
        } catch (Exception e) {
            e.printStackTrace();
            // Return an error response if capturing or decoding fails
            return ResponseEntity.status(500).body("Error capturing and decoding QR code.");
        }
    }
    
    @PostMapping("/captureAndScan")
    public ResponseEntity<String> captureAndScan(@RequestBody String imageDataUrl) {
        try {
            // Chuyển đổi base64 thành BufferedImage
            String[] parts = imageDataUrl.split(",");
            String base64Data = parts[1];
            byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
            ByteArrayInputStream bais = new ByteArrayInputStream(decodedBytes);
            BufferedImage bufferedImage = ImageIO.read(bais);

            // Các bước xử lý khác giống như trong phương thức captureQrCode gốc
            // ...
           // BufferedImage bufferedImage = webcam.getImage(); // Assuming selectedWebcam is defined

            // Convert BufferedImage to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            baos.flush();

            // Set the content type as image/png
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            // Decode QR code from the captured image
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
            Result result = new MultiFormatReader().decode(bitmap);

            // You can use the result.getText() to get the decoded QR code value
            String qrCodeValue = result.getText();

            // Perform any action with the decoded QR code value
            System.out.println("Decoded QR Code: " + qrCodeValue);

            // Return the decoded QR code value as part of the response
            return ResponseEntity.ok(qrCodeValue);
        } catch (Exception e) {
            e.printStackTrace();
            // Return an error response if capturing or decoding fails
            return ResponseEntity.status(500).body("Error capturing and decoding QR code.");
        }
    }

}
