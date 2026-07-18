package com.truchi.vastragruh.controller;

import com.truchi.vastragruh.dto.ImageUploadResponse;
import com.truchi.vastragruh.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageUploadController {

    private final ImageUploadService service;

    @PostMapping("/upload/{productId}")
    public ResponseEntity<ImageUploadResponse> upload(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file) {

        String imageUrl = service.upload(file,productId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ImageUploadResponse(imageUrl));
    }

    @PostMapping("/uploadImg")
    public ResponseEntity<ImageUploadResponse> uploadImage(
            @RequestParam("file") MultipartFile file) {

        String imageUrl = service.upload(file,null);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ImageUploadResponse(imageUrl));
    }


}