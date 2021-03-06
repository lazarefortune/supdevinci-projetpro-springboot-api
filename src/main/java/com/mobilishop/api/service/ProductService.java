package com.mobilishop.api.service;

import com.mobilishop.api.model.Product;

import java.util.List;

public interface ProductService {
    Product getProductById(Long id);
    Product getProductByName(String name);
    List<Product> searchProducts(String query);
    Product getProductByCategory(String categoryName);
    List<Product> getAllProducts();
    Product createOne(Product product);
    Product updateOne(Product product);
    void deleteOne(Long id);
    List<Product> getProductsByUser(Long user);
}
