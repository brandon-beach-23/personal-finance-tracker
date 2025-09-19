package com.example.wgu.finance_tracker_backend.services.interfaces;

import com.example.wgu.finance_tracker_backend.models.Category;

import java.util.List;

public interface CategoryService {
    CategoryService createCategory (Category category);
    List<Category> getCategories();
    Category getCategoryByTransactionId(Long id);
    Category updateCategory(Category category);
    void deleteCategory(Long id);
}
