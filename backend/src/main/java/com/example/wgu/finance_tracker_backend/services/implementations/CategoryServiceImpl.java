package com.example.wgu.finance_tracker_backend.services.implementations;

import com.example.wgu.finance_tracker_backend.DTOs.CategoryRequest;
import com.example.wgu.finance_tracker_backend.DTOs.CategoryResponse;
import com.example.wgu.finance_tracker_backend.exceptions.ResourceNotFoundException;
import com.example.wgu.finance_tracker_backend.models.Category;
import com.example.wgu.finance_tracker_backend.repositories.CategoryRepository;
import com.example.wgu.finance_tracker_backend.services.interfaces.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Optional<Category> existingCategory = categoryRepository.findByName(categoryRequest.getCategoryName());
        if (existingCategory.isPresent()) {
            throw new IllegalArgumentException("Category name already exists");
        }

        Category newCategory = new Category();
        newCategory.setName(categoryRequest.getCategoryName());
        Category savedCategory = categoryRepository.save(newCategory);

        return convertToDTO(savedCategory);
    }

    @Override
    public Optional<CategoryResponse> getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .map(this::convertToDTO);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CategoryResponse convertToDTO(Category savedCategory) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setCategoryId(savedCategory.getId());
        categoryResponse.setCategoryName(savedCategory.getName());
        return categoryResponse;
    }
}