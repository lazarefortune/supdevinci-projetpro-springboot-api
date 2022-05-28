package com.mobilishop.api.service.impl;

import com.mobilishop.api.exception.ApiRequestException;
import com.mobilishop.api.model.Category;
import com.mobilishop.api.repository.CategoryRepository;
import com.mobilishop.api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new ApiRequestException("Category not found", HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public Category getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name);
        if (category == null) {
            throw new ApiRequestException("Category not found with name: " + name, HttpStatus.NOT_FOUND);
        }
        return category;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category createOne(Category category) {
        Category existingCategory = categoryRepository.findByName(category.getName());
        if (existingCategory != null) {
            throw new ApiRequestException("Category already exists with name: " + category.getName(), HttpStatus.BAD_REQUEST);
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category updateOne(Category category) {
        Category existingCategory = categoryRepository.findByName(category.getName());
        if (existingCategory != null && !existingCategory.getId().equals(category.getId())) {
            throw new ApiRequestException("Category already exists with name: " + category.getName(), HttpStatus.BAD_REQUEST);
        }
        return categoryRepository.save(category);
    }

    @Override
    public void deleteOne(Long id) {
        Category existingCategory = categoryRepository.findById(id).orElse(null);
        if (existingCategory == null) {
            throw new ApiRequestException("Category not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> getCategoriesByUser(Long user) {
        // TODO: implement
        return null;
    }
}
