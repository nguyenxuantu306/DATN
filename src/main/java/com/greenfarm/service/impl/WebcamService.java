package com.greenfarm.service.impl;

import java.awt.image.BufferedImage;

import org.springframework.stereotype.Service;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

import jakarta.annotation.PostConstruct;

@Service
public class WebcamService {
	
	private Webcam webcam;

    @PostConstruct
    public void init() {
        // Chọn camera mặc định
        webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        
       // webcam.open();
    }

    public BufferedImage capture() {
        return webcam.getImage();
    }
//    private Webcam webcam;
//
//    public WebcamService() {
//        webcam = Webcam.getDefault();
//        webcam.setViewSize(WebcamResolution.VGA.getSize());
//        webcam.open();
//    }
//
//    public String readQRCodeFromCamera() {
//        JFrame window = new JFrame("QR Code Scanner");
//        window.setSize(800, 600);
//        window.setLayout(null);
//        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        WebcamPanel panel = new WebcamPanel(webcam);
//        panel.setBounds(0, 0, 800, 600);
//        window.add(panel);
//
//        window.setVisible(true);
//
//        try {
//            while (true) {
//                if (webcam.getImage() != null) {
//                    BufferedImage bufferedImage = webcam.getImage();
//
//                    LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
//                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
//
//                    Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
//                    hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
//
//                    try {
//                        Result result = new MultiFormatReader().decode(bitmap, hints);
//                        return result.getText();
//                    } catch (NotFoundException e) {
//                        // QR Code not found in this frame
//                    }
//
//                    Thread.sleep(100); // Thời gian chờ giữa các frame
//                }
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}