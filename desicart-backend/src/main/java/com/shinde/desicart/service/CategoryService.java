package com.shinde.desicart.service;

import com.shinde.desicart.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(Long id);
    CategoryDto createCategory(CategoryDto categoryDTO);
    CategoryDto updateCategory(Long id, CategoryDto categoryDTO);
    void deleteCategory(Long id);
}
