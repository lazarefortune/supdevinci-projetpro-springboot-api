package com.mobilishop.api.service;

import com.mobilishop.api.model.Category;

import java.util.List;

public interface CategoryService {
  Category getCategoryById(Long id);

  Category getCategoryByName(String name);

  List<Category> getAllCategories();

  Category createOne(Category category);

  Category updateOne(Category category);

  void deleteOne(Long id);

  List<Category> getCategoriesByUser(Long user);
}
