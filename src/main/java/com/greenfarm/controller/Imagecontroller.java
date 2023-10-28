//package com.greenfarm.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.greenfarm.service.IImageService;
//
//public class Imagecontroller {
//	@Autowired
//    IImageService imageService;
//
//
//    @PostMapping
//    public ResponseEntity<String> create(@RequestParam(name = "file") MultipartFile[] files) {
//
//            for (MultipartFile file : files) {
//
//                try {
//
//                    String fileName = imageService.save(file);
//
//                    String imageUrl = imageService.getImageUrl(fileName);
//
//                    // do whatever you want with that
//
//                } catch (Exception e) {
//                //  throw internal error;
//                }
//            }
//
//        return ResponseEntity.ok().build();
//    }
//}
