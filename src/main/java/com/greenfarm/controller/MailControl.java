package com.greenfarm.controller;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.greenfarm.service.EmailService;
import com.greenfarm.service.impl.WebcamService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/email")
public class MailControl {

	private static final Logger LOG = LoggerFactory.getLogger(MailControl.class);

	@Autowired
	EmailService emailService;

	@GetMapping(value = "/simple-email/{user-email}")
	public @ResponseBody ResponseEntity sendSimpleEmail(@PathVariable("user-email") String email) {

		try {
			emailService.sendSimpleEmail(email, "Welcome", "This is a welcome email for your!!");
		} catch (MailException mailException) {
			LOG.error("Error while sending out email..{}", mailException.getStackTrace());

			return new ResponseEntity<>("Unable to send email", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>("Please check your inbox", HttpStatus.OK);
	}

	@GetMapping(value = "/simpleemail/{user-email}")
	public @ResponseBody ResponseEntity sendEmailAttachment(@PathVariable("user-email") String email) {

		try {
			emailService.sendEmailWithAttachment(email, "Order Confirmation", "Thanks for your recent order", "1");
		} catch (MessagingException | FileNotFoundException mailException) {
			LOG.error("Error while sending out email..{}", mailException.getStackTrace());
			return new ResponseEntity<>("Unable to send email", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>("Please check your inbox for order confirmation", HttpStatus.OK);
	}
	
	
//	@Autowired
//    private WebcamService webcamService;
//
//    @GetMapping("/capture")
//    public String capture() {
//    	BufferedImage bufferedImage = webcamService.capture();
//       // return webcamService.capture();
////    	response.setContentType("image/png");
////    	ImageIO.write(bufferedImage, "png", response.getOutputStream());
//        return "readqrcode";
//    }
    
	 @GetMapping("/readqrcode")
	    public String readqrcode() {
	    	try {
	    		
				String path = "D:/FPTPOLYTECHNIC/DUANTOTNGHIEP/DATN/src/main/resources/qrcode/don29.png";
				
				BufferedImage bf = ImageIO.read(new FileInputStream(path));
				
				BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(
						new BufferedImageLuminanceSource(bf)));
				
				Result result = new MultiFormatReader().decode(bitmap);
				
				System.out.println(result.getText());
				return "redirect:/login";
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			return null;
			
	    }
}
