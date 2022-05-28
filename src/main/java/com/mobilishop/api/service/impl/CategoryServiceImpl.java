package com.mobilishop.api.service.impl;

import com.mobilishop.api.model.Category;
import com.mobilishop.api.repository.CategoryRepository;
import com.mobilishop.api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

  @Autowired CategoryRepository categoryRepository;

  @Override
  public Category getCategoryById(Long id) {
    return categoryRepository.findById(id).orElse(null);
    /* return categoryRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Category not found with id: " + id)
    );*/
  }

  @Override
  public Category getCategoryByName(String name) {
    return categoryRepository.findByName(name);
  }

  @Override
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  @Override
  public Category createOne(Category category) {
    return categoryRepository.save(category);
  }

  @Override
  public Category updateOne(Category category) {
    return categoryRepository.save(category);
  }

  @Override
  public void deleteOne(Long id) {
    categoryRepository.deleteById(id);
  }

  @Override
  public List<Category> getCategoriesByUser(Long user) {
    return null;
  }
}
