package com.greenfarm.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.greenfarm.service.impl.WebcamService;

@RestController
@RequestMapping("/api/camera")
@CrossOrigin(origins = "http://localhost:8080/")
public class CameraController {
    @Autowired
    private WebcamService webcamService;

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
    	Webcam selectedWebcam = webcams.get(2);
    	System.out.println(selectedWebcam);
    	
    	// Đóng camera hiện tại
    	
    	//webcam.close();

    	// Mở camera mới
    	selectedWebcam.open(true);

    	// Đổi sang camera khác (ví dụ: camera thứ hai)
    	//selectedWebcam = webcams.get(1);

        try {
            //BufferedImage bufferedImage = webcamService.capture();
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
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer( 
            		new BufferedImageLuminanceSource(bufferedImage)
            		));
            
            
            Result result = new MultiFormatReader().decode(bitmap);

            // You can use the result.getText() to get the decoded QR code value
            String qrCodeValue = result.getText();

            // Perform any action with the decoded QR code value
            System.out.println("Decoded QR Code: " + qrCodeValue);

            // You can return the decoded QR code value as part of the response
            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
            return "redirect:/"+qrCodeValue;
           // return ResponseEntity.ok(qrCodeValue);
        } catch (Exception e) {
            e.printStackTrace();
            //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error capturing and decoding QR code.");
            System.out.println("loi");
            return "/captureqr"; 
        }
    }
}
