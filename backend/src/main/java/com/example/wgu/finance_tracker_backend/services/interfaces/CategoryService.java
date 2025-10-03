package com.example.wgu.finance_tracker_backend.services.interfaces;

import com.example.wgu.finance_tracker_backend.DTOs.CategoryRequest;
import com.example.wgu.finance_tracker_backend.DTOs.CategoryResponse;
import com.example.wgu.finance_tracker_backend.models.Category;
import com.example.wgu.finance_tracker_backend.repositories.CategoryRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    CategoryResponse createCategory (CategoryRequest categoryRequest);
    Optional<CategoryResponse> getCategoryByName(String name);
    List<CategoryResponse> getAllCategories();
}
