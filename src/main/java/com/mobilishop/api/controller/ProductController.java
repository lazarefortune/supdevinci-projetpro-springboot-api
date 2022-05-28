package com.mobilishop.api.controller;

import com.mobilishop.api.model.Product;
import com.mobilishop.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

  @Autowired ProductService productService;

  @GetMapping("/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable Long id) {
    return ResponseEntity.ok(productService.getProductById(id));
  }

  @GetMapping("/name")
  public ResponseEntity<Product> getProductByName(@RequestParam String name) {
    return ResponseEntity.ok(productService.getProductByName(name));
  }

  @GetMapping("/search")
  public ResponseEntity<List<Product>> searchProducts(@RequestParam String query) {
    return ResponseEntity.ok(productService.searchProducts(query));
  }

  @GetMapping("/category/{category}")
  public Product getProductByCategory(@PathVariable String category) {
    return productService.getProductByCategory(category);
  }

  @GetMapping
  public List<Product> getAllProducts() {
    return productService.getAllProducts();
  }

  @PostMapping
  public ResponseEntity<Product> createOne(@RequestBody Product product) {
    return ResponseEntity.ok(productService.createOne(product));
    // return null;
  }

  @PutMapping("/{id}")
  public ResponseEntity<Product> updateOne(@RequestBody Product product) {
    // return productService.updateOne(id, name);
    return null;
  }

  @DeleteMapping("/{id}")
  public void deleteOne(@PathVariable Long id) {
    productService.deleteOne(id);
  }

  @GetMapping("/user/{userId}")
  public List<Product> getProductsByUser(@PathVariable Long userId) {
    return productService.getProductsByUser(userId);
  }
}
