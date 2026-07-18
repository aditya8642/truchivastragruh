package com.truchi.vastragruh.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {

    String upload(MultipartFile file, Long productId);

    void delete(String publicId);
}