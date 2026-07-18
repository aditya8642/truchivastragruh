package com.truchi.vastragruh.service;

 import com.truchi.vastragruh.entity.Product;
 import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Product create(Product product);

    Product update(Long id, Product product);

    void delete(Long id);

    Product getById(Long id);

    Page<Product> getAll(Pageable pageable);

    Page<Product> search(String keyword, Pageable pageable);

    Page<Product> getByCategory(Long categoryId, Pageable pageable);
}