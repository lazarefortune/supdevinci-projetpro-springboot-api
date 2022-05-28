package com.mobilishop.api.service.impl;

import com.mobilishop.api.model.Product;
import com.mobilishop.api.repository.ProductRepository;
import com.mobilishop.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired ProductRepository productRepository;

  @Override
  public Product getProductById(Long id) {
    return productRepository.findById(id).orElse(null);
  }

  @Override
  public Product getProductByName(String name) {
    return productRepository.findByName(name);
  }

  // Search by name, description, category, author
  @Override
  public List<Product> searchProducts(String query) {
    return productRepository.searchProducts(query);
  }

  @Override
  public Product getProductByCategory(String categoryName) {
    // TODO: implement
    // Check if category exists

    // find all products with categoryName

    // Select * from product where category_name = categoryName
    return null;
  }

  @Override
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  @Override
  public Product createOne(Product product) {
    return productRepository.save(product);
  }

  @Override
  public Product updateOne(Product product) {
    return productRepository.save(product);
  }

  @Override
  public void deleteOne(Long id) {
    productRepository.deleteById(id);
  }

  @Override
  public List<Product> getProductsByUser(Long userId) {
    return null;
  }
}
