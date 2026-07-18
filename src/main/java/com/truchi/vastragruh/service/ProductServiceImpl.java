package com.truchi.vastragruh.service;


import com.truchi.vastragruh.entity.Category;
import com.truchi.vastragruh.entity.Product;
import com.truchi.vastragruh.entity.ProductImage;
import com.truchi.vastragruh.repository.CategoryRepository;
import com.truchi.vastragruh.repository.ProductImageRepository;
import com.truchi.vastragruh.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public Product create(Product product) {

        Long categoryId = product.getCategory().getId();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category not found"));

        product.setCategory(category);


        return repository.save(product);
    }

    @Override
    public Product update(Long id, Product product) {

        Product existing = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Product not found"));

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());
        existing.setActive(product.getActive());

        if (product.getCategory() != null) {

            Category category = categoryRepository.findById(
                    product.getCategory().getId())
                    .orElseThrow(() ->
                            new EntityNotFoundException("Category not found"));

            existing.setCategory(category);
        }

        return repository.save(existing);
    }

    @Override
    public void delete(Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Product not found"));

        repository.delete(product);
    }

    @Override
    public Product getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Product not found"));
    }

    @Override
    public Page<Product> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Product> search(String keyword, Pageable pageable) {
        return repository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    @Override
    public Page<Product> getByCategory(Long categoryId,
                                       Pageable pageable) {
        return repository.findByCategoryId(categoryId, pageable);
    }
}