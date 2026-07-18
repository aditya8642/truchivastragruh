package com.truchi.vastragruh.service;


import com.truchi.vastragruh.entity.Category;
import com.truchi.vastragruh.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    public Category create(Category category) {

        if (repository.existsByNameIgnoreCase(category.getName())) {
            throw new IllegalArgumentException("Category already exists");
        }

        return repository.save(category);
    }

    @Override
    public Category update(Long id, Category category) {

        Category existing = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category not found"));

        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        existing.setActive(category.getActive());

        return repository.save(existing);
    }

    @Override
    public void delete(Long id) {

        Category category = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category not found"));

        repository.delete(category);
    }

    @Override
    public Category getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category not found"));
    }

    @Override
    public List<Category> getAll() {

        return repository.findAll();
    }
}