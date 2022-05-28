package com.mobilishop.api.service.impl;

import com.mobilishop.api.exception.ApiRequestException;
import com.mobilishop.api.model.Category;
import com.mobilishop.api.model.Product;
import com.mobilishop.api.repository.ProductRepository;
import com.mobilishop.api.service.CategoryService;
import com.mobilishop.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryService categoryService;

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new ApiRequestException("Product not found", HttpStatus.NOT_FOUND)
        );

    }

    @Override
    public Product getProductByName(String name) {
        Product product = productRepository.findByName(name);
        if (product == null) {
            throw new ApiRequestException("Product not found", HttpStatus.NOT_FOUND);
        }
        return product;
    }

    // Search by name, description, category, author
    @Override
    public List<Product> searchProducts(String query) {
        List<Product> products = productRepository.searchProducts(query);
        if (products.isEmpty()) {
            throw new ApiRequestException("No products found", HttpStatus.NOT_FOUND);
        }
        return products;
    }
    @Override
    public List<Product> getProductsByCategory(String categoryName) {
        Category category = categoryService.getCategoryByName(categoryName);
        if (category == null) {
            throw new ApiRequestException("Category not found", HttpStatus.NOT_FOUND);
        }
        List<Product> products = productRepository.getProductsByCategory(categoryName);

        return products;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    @Override
    public Product createOne(Product product) {
        // Check if product already exists
        Product existingProduct = productRepository.findByName(product.getName());
        if (existingProduct != null) {
            throw new ApiRequestException("Product already exists", HttpStatus.CONFLICT);
        }

        Category category = categoryService.getCategoryByName(product.getCategory().getName());
        if (category == null) {
            throw new ApiRequestException("Category not found", HttpStatus.NOT_FOUND);
        }
        product.setCategory(category);

        return productRepository.save(product);
    }

    @Override
    public Product updateOne(Long productId, Product product) {
        Product existingProduct = productRepository.findById(productId).orElse(null);
        if (existingProduct == null) {
            throw new ApiRequestException("Product not found", HttpStatus.NOT_FOUND);
        }

        // Check if product already exists
        Product existingProduct2 = productRepository.findByName(product.getName());
        if (existingProduct2 != null && !existingProduct2.getId().equals(productId)) {
            throw new ApiRequestException("Product already exists", HttpStatus.CONFLICT);
        }

        Category category = categoryService.getCategoryByName(product.getCategory().getName());
        if (category == null) {
            throw new ApiRequestException("Category not found", HttpStatus.NOT_FOUND);
        }
        existingProduct.setCategory(category);
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setImage(product.getImage());

        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteOne(Long id) {
        // check if product exists
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new ApiRequestException("Product not found", HttpStatus.NOT_FOUND);
        }

        productRepository.deleteById(id);
    }

    @Override
    public List<Product> getProductsByUser(Long userId) {
        return null;
    }
}
