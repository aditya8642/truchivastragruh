package com.truchi.vastragruh.service;

import com.cloudinary.Cloudinary;
import com.truchi.vastragruh.entity.Product;
import com.truchi.vastragruh.entity.ProductImage;
import com.truchi.vastragruh.repository.ProductImageRepository;
import com.truchi.vastragruh.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageUploadServiceImpl implements ImageUploadService {


    private final Cloudinary cloudinary;
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;


    @Override
    public String upload(MultipartFile file, Long productId) {

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of(
                            "folder", "fashion-store"
                    ));

            if(Objects.nonNull(productId)){
                Optional<Product> product =  productRepository.findById(productId);
                ProductImage productImage = new ProductImage();
                productImage.setProduct(product.get());
                productImage.setImageUrl(uploadResult.get("url").toString());
                productImageRepository.save(productImage);
            }
            return uploadResult.get("secure_url").toString();

        } catch (IOException e) {
            throw new RuntimeException("Unable to upload image", e);
        }
    }

    @Override
    public void delete(String publicId) {

        try {
            cloudinary.uploader().destroy(publicId, Map.of());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}