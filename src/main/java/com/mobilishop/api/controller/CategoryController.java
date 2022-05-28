package com.mobilishop.api.controller;

import com.mobilishop.api.model.Category;
import com.mobilishop.api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

  @Autowired CategoryService categoryService;

  @GetMapping("/{id}")
  public Category getCategoryById(@PathVariable Long id) {
    return categoryService.getCategoryById(id);
  }

  @PostMapping("/name")
  public Category getCategoryByName(@RequestParam String name) {
    System.out.println("name: " + name);
    return categoryService.getCategoryByName(name);
  }

  @GetMapping
  public List<Category> getAllCategories() {
    return categoryService.getAllCategories();
  }

  @GetMapping("/user/{userId}")
  public List<Category> getCategoriesByUser(@PathVariable Long userId) {
    return categoryService.getCategoriesByUser(userId);
  }

  @PutMapping("/{categoryId}")
  public ResponseEntity<Category> updateOne(
      @PathVariable Long categoryId, @RequestBody Category category) {
    Category existsCategory = categoryService.getCategoryById(categoryId);
    // TODO: gestion des erreurs
    if (existsCategory == null) {
      return ResponseEntity.notFound().build();
    }

    existsCategory.setName(category.getName());
    existsCategory.setDescription(category.getDescription());
    existsCategory.setImage(category.getImage());

    return ResponseEntity.ok(categoryService.updateOne(existsCategory));
  }

  @PostMapping
  public Category createOne(@RequestBody Category category) {
    return categoryService.createOne(category);
  }

  @DeleteMapping("/{id}")
  public void deleteOne(@PathVariable Long id) {
    categoryService.deleteOne(id);
  }
}
