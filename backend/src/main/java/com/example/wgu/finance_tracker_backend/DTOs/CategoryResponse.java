package com.example.wgu.finance_tracker_backend.DTOs;

public class CategoryResponse {
    private String categoryName;
    private Long categoryId;

    public CategoryResponse() {
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


}
